library(dplyr)
library(org.Hs.eg.db)
uniprot <- as.data.frame(org.Hs.egUNIPROT)

`I2D.Uniprot` <- read.delim("/media/sf_Projects/KPM/kpm-web/web-app/default_data/graphs/I2D-Uniprot.sif", header=FALSE)

I2D <- left_join(I2D.Uniprot, uniprot, by=c("V1"="uniprot_id"))
colnames(I2D)[4] <- "source"
I2D <- left_join(I2D, uniprot, by=c("V3"="uniprot_id"))
colnames(I2D)[5] <- "target"
I2D_new <- I2D[,c("source", "V2", "target")]

#many I2D uniprot ids do not match to any entrez gene id. I think this is mostly because uniprot also contains partial proteins etc.
#In any case, we need to remove entries with NA

I2D_new <- na.omit(I2D_new)

write.table(I2D_new, file.choose(),quote = F,sep = "\t",row.names = F, col.names = F)
