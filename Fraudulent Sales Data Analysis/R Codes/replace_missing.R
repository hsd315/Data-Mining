load("/Users/eshan/Desktop/DMHW4/DMHW4_P1/sales.RData")

#mean
sales$Quant[is.na(sales$Quant)] <- as.integer(mean(sales$Quant,na.rm=TRUE))
sales$Val[is.na(sales$Val)] <- as.integer(mean(sales$Val,na.rm=TRUE))
#duplicate
v <- duplicated(sales)
duplicate <-sales[v,c("ID","Prod","Quant","Val","Insp")]
cleaned_data<-subset(sales,!duplicated(sales))
cleaned_data$UnitPrice <- cleaned_data$Val/cleaned_data$Quant
write.table(cleaned_data,"replaced_data_mean.csv",sep=",",row.names=FALSE)

#median
sales$Quant[is.na(sales$Quant)] <- as.integer(median(sales$Quant,na.rm=TRUE))
sales$Val[is.na(sales$Val)] <- as.integer(median(sales$Val,na.rm=TRUE))
summary(sales)
#duplicate
v <- duplicated(sales)
duplicate <-sales[v,c("ID","Prod","Quant","Val","Insp")]
cleaned_data<-subset(sales,!duplicated(sales))
cleaned_data$UnitPrice <- cleaned_data$Val/cleaned_data$Quant
write.table(cleaned_data,"replaced_data_median.csv",sep=",",row.names=FALSE)

#random
sales$Quant[is.na(sales$Quant)] <- sample(100:10000,,replace=T)
sales$Val[is.na(sales$Val)] <- sample(min(sales$Val):max(sales$Val),replace=T)
summary(sales)
#duplicate
v <- duplicated(sales)
duplicate <-sales[v,c("ID","Prod","Quant","Val","Insp")]
cleaned_data<-subset(sales,!duplicated(sales))
cleaned_data$UnitPrice <- cleaned_data$Val/cleaned_data$Quant
write.table(cleaned_data,"replaced_data_random.csv",sep=",",row.names=FALSE)