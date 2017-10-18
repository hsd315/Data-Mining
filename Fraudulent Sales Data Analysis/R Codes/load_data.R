load("/Users/eshan/Desktop/DMHW4/DMHW4_P1/sales.RData")
summary(sales)
str(sales)

table(sales$Insp)

#NA check
na_sales <- is.na(sales)
na_sales <-sales[!complete.cases(sales),]
table(na_sales$Insp)
#remove missing 
length(which(is.na(sales$Quant) & is.na(sales$Val)))
library("timeSeries")
removed_missing <-sales[complete.cases(sales),]
write.table(removed_missing,"sales_removing_missing_data.csv",sep=",",row.names=FALSE)
dim(removed_missing)
table(removed_missing$Insp)

#remove duplicate
v <- duplicated(removed_missing)
table(v)
table(sales$Insp)
table(v)
str(removed_missing)
duplicate <-removed_missing[v,c("ID","Prod","Quant","Val","Insp")]
table(duplicate$Insp)
print(duplicate)

cleaned_data<-subset(removed_missing,!duplicated(removed_missing))
cleaned_data$UnitPrice <- cleaned_data$Val/cleaned_data$Quant
write.table(cleaned_data,"cleaned_sales_data.csv",sep=",",row.names=FALSE)

plot(log(Quant)~log(Val),data=cleaned_data, col=rgb(0,0,0,0.5),pch=16)
table(cleaned_data$Quant > 10000000)
possible_outliers <- cleaned_data[cleaned_data$Quant > 10000000,c("ID","Prod","Quant","Val","Insp")]
table(possible_outliers$Insp)
table(cleaned_data$Val > 3000000)
possible_outliers <- cleaned_data[cleaned_data$Val > 4000000,c("ID","Prod","Quant","Val","Insp")]
table(possible_outliers$Insp)

trainData<- cleaned_data[cleaned_data$Insp!="unkn",c("ID","Prod","Quant","Val","Insp","UnitPrice")]
data_ok <-trainDatatr$Insp!="unkn",c("ID","Prod","Quant","Val","Insp","UnitPrice")]
summary(data_ok)
data_fraud<-which(trainData$Insp="fraud")
data_ok$Insp<-as.numeric(data_ok$Insp)
summary(data_ok)
table(data_ok$Insp)
plot(log(Quant)~log(Val),data=data_ok, col=data_ok$Insp+7,pch=16)




