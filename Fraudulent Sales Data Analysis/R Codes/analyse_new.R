load("/home/touahmed/Desktop/Dropbox/DMHW4/DMHW4_P1/sales.RData")

nlevels(sales$ID)
nlevels(sales$Prod)


library("Hmisc")
describe(sales)


totS <- table(sales$ID)
totP <- table(sales$Prod)
barplot(totS, main = "Transactions per salespeople", names.arg = "",
        xlab = "Salespeople", ylab = "Amount")
barplot(totP, main = "Transactions per product", names.arg = "",
        xlab = "Products", ylab = "Amount")

na_sales <-sales[!complete.cases(sales),]
table(na_sales$Insp)
describe(na_sales$Insp)