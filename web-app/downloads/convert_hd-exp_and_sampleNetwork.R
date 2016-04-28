library(dplyr)
library(org.Hs.eg.db)
gene_symbols <- as.data.frame(org.Hs.egALIAS2EG)

### sampleNetwork ###

sampleNetwork <- read.delim("/media/sf_Projects/KPM/kpm-web/web-app/downloads/sampleNetwork.sif", header=FALSE)
sampleNetwork <- left_join(sampleNetwork, gene_symbols, by=c("V1"="alias_symbol"))
colnames(sampleNetwork)[4] <- "source"
sampleNetwork <- left_join(sampleNetwork, gene_symbols, by=c("V3"="alias_symbol"))
sampleNetwork_new <- sampleNetwork[,c("source", "V2", "gene_id")]

write.table(sampleNetwork_new, file.choose(),quote = F,sep = "\t",row.names = F, col.names = F)


### hd.exp dataset ###

hd.exp <- read.delim("/media/sf_Projects/KPM/kpm-web/web-app/downloads/hd-exp.txt", header=FALSE, stringsAsFactors=FALSE)

hd.exp.genes <- left_join(data.frame(genes=hd.exp[,1]), gene_symbols, by=c("genes"="alias_symbol"))

#this dataset has been converted to gene symbols before but some ids did not match. we thus have some entries that already have entrez ids. we like to keep thos
within(hd.exp.genes, { gene_id[is.na(gene_id)] = as.integer(genes[is.na(gene_id)]) })

#gene symbols will have multiple matches, we take the first one which is the one with the smaller id and therefore the most likely match
hd.exp.genes <- hd.exp.genes[!duplicated(hd.exp.genes$genes),]

#3 genes are duplicated, we loose them
hd.exp <- hd.exp[!duplicated(hd.exp[,1]),]
hd.exp[,1] <- hd.exp.genes$gene_id

#some entries are NAs, we loose more
hd.exp <- filter(hd.exp, !is.na(V1))

write.table(hd.exp, file.choose(),quote = F,sep = "\t",row.names = F, col.names = F)
