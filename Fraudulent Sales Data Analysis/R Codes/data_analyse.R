cleaned_data<-read.table("cleaned_sales_data.csv",sep=",",header=TRUE)

summary(cleaned_data)
mean(cleaned_data$Quant)
mean(cleaned_data$Val)
mean(cleaned_data$UnitPrice)
sd(cleaned_data$Quant)
sd(cleaned_data$Val)
sd(cleaned_data$UnitPrice)
min(cleaned_data$UnitPrice)

trainData<- cleaned_data[cleaned_data$Insp!="unkn",c("ID","Prod","Quant","Val","Insp","UnitPrice")
summary(trainData)
scale(trainData[,c("Quant","Val")])

mean(trainData$Quant)
sd(trainData$Quant)

mean(trainData$Val)
sd(trainData$Val)

mean(trainData$UnitPrice)
sd(trainData$UnitPrice)                   
                         
data_ok <-trainData[trainData$Insp=="ok",c("ID","Prod","Quant","Val","Insp","UnitPrice")]
write.table(trainData,"trainData.csv",sep=",",row.names=FALSE)
summary(data_ok)
data_fraud<-trainData[trainData$Insp=="fraud",c("ID","Prod","Quant","Val","Insp","UnitPrice")]
summary(data_fraud)
layout(matrix(1:2, ncol=2))
totPd<-table(data_ok$Prod)
barplot(totP)

summary(data_fraud$ID)


totfid<-table(data_fraud$ID)
write.table(totfid,"ID_fraud.csv",sep=",",row.names=FALSE)

totfd<-table(data_fraud$Prod)
write.table(totfd,"prod_fraud.csv",sep=",",row.names=FALSE)

totfid<-table(data_ok$ID)
write.table(totfid,"ID_ok.csv",sep=",",row.names=FALSE)

totfd<-table(data_ok$Prod)
write.table(totfd,"prod_ok.csv",sep=",",row.names=FALSE)

mean(data_fraud$UnitPrice)
var(data_fraud$UnitPrice)
sd(data_fraud$UnitPrice)

mean(data_ok$UnitPrice)
var(data_ok$UnitPrice)
sd(data_ok$UnitPrice)

mean(data_fraud$Quant)ß
var(data_fraud$Quant)
sd(data_fraud$Quant)
                         
mean(data_fraud$Val)
mean(data_ok$Val)

mean(data_ok$Quant)
var(data_ok$Quant)
sd(data_ok$Quant)

barplot(totfd)
plot(log(Quant)~log(Val),data=data_fraud)
table(data_ok$ID)
tops <- data_ok[Prod %in% topP[1, ], c("Prod", "UnitPrice")]
tops$Prod <- factor(tops$Prod)
boxplot(Uprice ~ Prod, data = tops, ylab = "Uprice", log = "y")