package fileParsers
import dk.sdu.kpm.KPMSettings
import dk.sdu.kpm.graph.KPMGraph

import java.util.concurrent.ConcurrentHashMap

public class GraphFileParserService     {

    public enum Separator{
          TAB, COMMA, SPACE
    }

    public KPMSettings createAndSetGraph(KPMSettings kpmSettings, String graphFile, Separator separator) {
        def backNodesMap = new ConcurrentHashMap<String, Set<String>>();
        def backNodesByExpMap = new ConcurrentHashMap<String, Set<String>>();
        def backGenesMap = new ConcurrentHashMap<String, Set<String>>();
        def numCasesMap = new ConcurrentHashMap<String, Integer>();
        def numGenesMap = new ConcurrentHashMap<String, Integer>();
        def avgExpressedCasesMap = new ConcurrentHashMap<String, Double>();
        def avgExpressedGenesMap = new ConcurrentHashMap<String, Double>();
        def totalExpressedMap = new ConcurrentHashMap<String, Integer>();

        for (String expId : kpmSettings.MATRIX_FILES_MAP.keySet()) {
            backGenesMap.put(expId, new HashSet());
            numCasesMap.put(expId, 0);
        }

        String sep = "";
        if(separator == Separator.TAB){
            sep = "\t";
        }else if(separator == Separator.COMMA){
            sep = ",";
        }else if(separator == Separator.SPACE){
            sep = " ";
        }

        HashMap<String, String> nodeId2Symbol = new HashMap<String, String>();
        Map<String, Map<String, int[]>> expressionMap = new HashMap<String, Map<String, int[]>>();
        LinkedList<String[]> edgeList = new LinkedList<String[]>();
        HashMap<String, Integer> without_exp = new HashMap<String, Integer>();
        HashSet<String> inNetwork = new HashSet<String>();
        for (String fileId : kpmSettings.MATRIX_FILES_MAP.keySet()) {
            numCasesMap.put(fileId, 0);
            without_exp.put(fileId, 0);
        }

            def line = "";
            BufferedReader graphReader = new BufferedReader(new StringReader(graphFile));
            int cont = 0;
            def linesFailed = [];
            while ((line = graphReader.readLine()) != null) {
                String[] fields = line.split("\t");

                if (fields.length < 3) {
                    log.error("""LINE NUMBER: ${cont} of ${kpmSettings.MAIN_CYNETWORK_ID} could not be parsed\n
                    ${line}
                    """);
                    linesFailed << cont
                }
                else {
                    String id1 = fields[0].trim();
                    nodeId2Symbol.put(id1, id1);

                    String id2 = fields[2].trim();
                    nodeId2Symbol.put(id2, id2);

                    def edge = new String[2];
                    edge[0] = id1;
                    edge[1] = id2;
                    edgeList.add(edge);
                    inNetwork.add(id1);
                    inNetwork.add(id2);
                }
                cont++;
            }

            for (String fileId : kpmSettings.MATRIX_FILES_MAP.keySet()) {
                int totalExp = 0;
                int numCases = 0;
                int numGenes = 0;
                Set<String> inExp = new HashSet<String>();

                BufferedReader expressionReader = new BufferedReader(new StringReader(kpmSettings.MATRIX_FILES_MAP.get(fileId)));

                while ((line = expressionReader.readLine()) != null) {
                    numGenes++;
                    String[] fields = line.split(sep);
                    String nodeId = fields[0].trim();
                    inExp.add(nodeId);

                    int[] exp = new int[fields.length - 1];

                    for (int i = 1; i < fields.length; i++) {
                        String val = fields[i].trim();
                        if (val.equals("1")) {
                            exp[i - 1] = 1;
                            totalExp++;
                        } else if (val.equals("-1")) {
                            exp[i - 1] = -1;
                            totalExp++;
                        } else {
                            exp[i - 1] = 0;
                        }
                    }

                    if (expressionMap.containsKey(nodeId)) {
                        expressionMap.get(nodeId).put(fileId, exp);
                    } else {
                        Map<String, int[]> aux = new HashMap<String, int[]>();
                        aux.put(fileId, exp);
                        expressionMap.put(nodeId, aux);
                    }
                    numCases = exp.length;
                    numCasesMap.put(fileId, numCases);
                }
                totalExpressedMap.put(fileId, totalExp);
                double avgExpCases = 0;
                double avgExpGenes = 0;
                if (totalExp > 0) {
                    avgExpCases = (double)numCases / (double)totalExp;
                    avgExpGenes = (double)numGenes / (double)totalExp;
                }
                numGenesMap.put(fileId, inExp.size());
                avgExpressedCasesMap.put(fileId, avgExpCases);
                avgExpressedGenesMap.put(fileId, avgExpGenes);
                Set<String> bckN = new HashSet(inNetwork);
                Set<String> bckG = new HashSet(inExp);
                for (String id: inNetwork) {
                    if (inExp.contains(id)) {
                        bckN.remove(id);
                    }
                }
                for (String id: inExp) {
                    if (inNetwork.contains(id)) {
                        bckG.remove(id);
                    }
                }

                backNodesByExpMap.put(fileId, bckN);
                backGenesMap.put(fileId, bckG);
                expressionReader.close();
            }

            graphReader.close();

        for (String nodeId : inNetwork) {
            if (expressionMap.containsKey(nodeId)) {
                Map<String, int[]> expMap = expressionMap.get(nodeId);
                for (String expId : kpmSettings.MATRIX_FILES_MAP.keySet()) {
                    if (!expMap.containsKey(expId)) {
                        if (backNodesMap.containsKey(nodeId)) {
                            backNodesMap.get(nodeId).add(expId);
                        } else {
                            HashSet<String> aux = new HashSet<String>();
                            aux.add(expId);
                            backNodesMap.put(nodeId, aux);
                        }

                    }
                }
            } else {
                if (backNodesMap.containsKey(nodeId)) {
                    backNodesMap.get(nodeId).addAll(kpmSettings.MATRIX_FILES_MAP.keySet());
                } else {
                    HashSet<String> aux = new HashSet<String>();
                    aux.addAll(kpmSettings.MATRIX_FILES_MAP.keySet());
                    backNodesMap.put(nodeId, aux);
                }
            }
        }

        kpmSettings.NUM_CASES_MAP = numCasesMap;
        kpmSettings.NUM_STUDIES = numCasesMap.size();
        kpmSettings.MAIN_GRAPH = new KPMGraph(expressionMap, edgeList, nodeId2Symbol, backNodesMap, backGenesMap, kpmSettings.NUM_CASES_MAP);

        //Refresh the graph immediately
        kpmSettings.MAIN_GRAPH.refreshGraph(kpmSettings);

        return kpmSettings;
    }
}
