# CSCI-B 565 DATA MINING

# Breast Cancer Data Set

- 1. The data set ∆ is collected from the University of Wisconsin Hospitals, Madison from Dr. William

H. Wolberg. The size of ∆ is 699. The data set can be used to detect the malignant cancer or it ispossible to identify which breast cancers are malignant and which breast cancers are benign. Thereare 10 attributes for each elements of the data set. And the cancer type actually depends on thisattributes. It is possible that some attributes might useful to detect the type of cancer and some arenot. We can run some known data mining approach on this data set and using those data miningapproach we can learn about the data and then we can use them to identify cancer types. If We usesome portion of the data set as a training set and the rest of the sets as the test data set, then wecan use the training set to learn about the problem and the test data set can help us to evaluate ourapproach. And it is possible to achieve more than 90% of accuracy for predicting the type of cancerusing some well known data mining algorithms.

Here the size of ∆ is 699.

(a) No, this is not a large data set. If we compare the dataset with some other real life problem’sdata sets, we will realize it is pretty small.The problem given in problem 3 has 761K data. Ifwe consider some medical experimental data set, like for the Colon cancer data set it has almost2000 gene samples the Leukemia data set has almost 7000 gene samples. If we consider some otherdomains, like for the Network Intrusion detection data set has almost 8 million sample records1.So, considering other common data mining problem it has pretty small data set.

(b) There are 10 attributes excluding the Sample Code Number(SCN) in data sets.This is a prettylarge number of attributes considering the data size. The astronomy data set has 6 attributesfor 761K data sets. This number of attributes might aﬀect the performance of the data miningalgorithms like ID3. For the network intrusion data, though it has 41 attributes but the totalnumber of data set for this problem is 8 million.

- 2. If we ignore the Sample code number (SCN) there are 10 attributes. They are Clump Thickness,Uniformity

of Cell Size, Uniformity of Cell Shape,Marginal Adhesion,Single Epithelial Cell Size,Bare Nuclei,BlandChromatin,Normal Nucleoli,Mitoses and Class.

- 3. There are 16 missing values. The following R code will give the SCNs for the missing values and give

us ∆∗ which is the cleaned data set for ∆ where missing values are removed. The missing SCNs areprinted at the end of the code and ∆∗ is stored in a separate csv ﬁle.I observed that the attribute BareNuclei only contains the missing value.

> cancer_data <- read.table("breast-cancer-wisconsin.data",sep=",",na.strings="?")> names(cancer_data) <- c("SCN","Clump Thickness","Uniformity of Cell Size",

+ "Uniformity of Cell Shape","Marginal Adhesion","Single Epithelial Cell Size","Bare Nuclei",+ "Bland Chromatin","Normal Nucleoli","Mitoses","Class")

> write.table(cancer_data,"cancer_data_before_cleaning.csv",sep=",",row.names=FALSE)> str(cancer_data)

> install.packages("timeSeries")

> library("timeSeries")

> missing_values <- is.na(cancer_data["Bare Nuclei"])

> missing_SCN <- cancer_data[missing_values, c("SCN")]

> cleaned_data <-cancer_data[complete.cases(cancer_data),]

> write.table(cleaned_data,"cancer_data_after_cleaning.csv",sep=",",row.names=FALSE)> missing_SCN

[1] 1057013 1096800 1183246 1184840 1193683 1197510 1241232 169356

[9] 432809 563649 606140

61634 704168 733639 1238464 1057067

- 1University of edinburgh website(http://www.inf.ed.ac.uk/teaching/courses/dme/html/datasets0405.html)

Moreover, I have found some duplicates. There are 8 duplicate data in the ﬁle. I removed it using thefollowing R code. It will give the SCNs of the duplicate data and store the pruned data into a separatecsv ﬁle. I used that csv ﬁle with 675 data in my k-means and ID3 algorithm.

> cleaned_data <-cancer_data[complete.cases(cancer_data),]

> cleaned_data <- read.table("cancer_data_after_cleaning.csv",header=TRUE, sep=",")> v <- duplicated(cleaned_data)

> duplicate <-cleaned_data[v,c("SCN")]

> pruned<-subset(cleaned_data,!duplicated(cleaned_data))

> write.table(pruned,"cancer_data_after_pruning.csv",sep=",", row.names=FALSE)

> summary(v)

Mode

logical

FALSE

TRUE

NAs

> print(duplicate)

[1] 1218860 1100524 1116116 1198641 320675 704097 1321942 466906

The data with SCN 1198641 has three instances where two of them are duplicate and the rest hassame SCN but diﬀerent attribute values. As SCN 1198642 was not assigned, I replaced that SCN with1198642.

For replacing the missing values I used two approaches.

I replaced them using mean,median andrandom numbers between 1 to 10. I have written the following R code for replacing the missing valuesusing mean,median and random values. The ﬁnal table is included after the R code.

> data <- read.table("cancer_data_before_cleaning.csv", header=TRUE, sep=",")

> cleaned_data <- read.table("cancer_data_after_cleaning.csv", header=TRUE, sep=",")> data$Bare.Nuclei[is.na(data$Bare.Nuclei)] <- mean(data$Bare.Nuclei,na.rm=TRUE)> write.table(cancer_data,"cancer_data_after_replace_mean.csv", sep=",", row.names=FALSE)> data <- read.table("cancer_data_before_cleaning.csv", header=TRUE, sep=",")

> cleaned_data <- read.table("cancer_data_after_cleaning.csv", header=TRUE, sep=",")> data\Bare.Nuclei[is.na(data$Bare.Nuclei)] <- median(data$Bare.Nuclei,na.rm=TRUE)> write.table(data,"cancer_data_after_replace_median.csv", sep=",",row.names=FALSE)> data <- read.table("cancer_data_before_cleaning.csv", header=TRUE, sep=",")

> cleaned_data <- read.table("cancer_data_after_cleaning.csv", header=TRUE, sep=",")> data$Bare.Nuclei[is.na(data$Bare.Nuclei$)] <- sample(1:10,16,replace=T)

> write.table(data,"cancer_data_after_replace_random.csv", sep=",",row.names=FALSE)The ﬁnal data is presented in Table 1.

(a) The ratio of the missing data is 0.022. So, from my opinion the amount of missing data is notsigniﬁcant. The removal of missing data from the actual data set will reduce the total no ofdata values. In data mining algorithms performs better with large data. But, 16 out of 699 isnot a large number and may be insigniﬁcant. If the ratio is greater than 0.1, then it might getsigniﬁcant.

(b) In my algorithms, I used the data set by removing the tuples with unknown data. I have run it byreplacing the values using mean, median and random values. Table 2 gives the result of k-meansalgorithm where the data set was created by removing the unknown data.

It performs betterwhen all the attributes were used and falls gradually when we remove some of the attributes.Row

SCN

Ai

Datamean Datamedian Datarandom

1057013 Bare Nuclei

1096800 Bare Nuclei

1183246 Bare Nuclei

1184840 Bare Nuclei

1193683 Bare Nuclei

1197510 Bare Nuclei

1241232 Bare Nuclei

Bare Nuclei

Bare Nuclei

Bare Nuclei

Bare Nuclei

Bare Nuclei

Bare Nuclei

1238464 Bare Nuclei

1057067 Bare Nuclei

Table 1: Final Data set

Ckm=2(∆∗) P P VEuclidean P P VM anhattan

A1, . . . , A9

A1, . . . , A7

A1, . . . , A5

A1, . . . , A3

0.9600

0.9600

0.9437

0.9407

0.8593

0.9467

0.9556

0.9333

0.9363

0.8252

A1, A2

Table 2: PPV values when unknown data is removed

Table 3 is generated by running V -fold cross validation on the same data set. If we look at theresults we can found that the PPV is increasing when we are removing some attributes. Theweighted PPV 1

P P Vi = .9601 for eucllidean distance and 0.9527 for manhattan distance.10(cid:80)

i=1

Train

Test

Ckm=2(D∗ − d∗

Ckm=2(d∗

1)

1)

Ckm=2(D∗ − d∗

Ckm=2(d∗

2)

2)

Ckm=2(D∗ − d∗

Ckm=2(d∗

3)

3)

Ckm=2(D∗ − d∗

Ckm=2(d∗

4)

4)

Ckm=2(D∗ − d∗

Ckm=2(d∗

5)

5)

Ckm=2(D∗ − d∗

Ckm=2(d∗

6)

6)

Ckm=2(D∗ − d∗

Ckm=2(d∗

7)

7)

Ckm=2(D∗ − d∗

Ckm=2(d∗

8)

8)

Ckm=2(D∗ − d∗

Ckm=2(d∗

9)

9)

Ckm=2(D∗ − d∗

10) Ckm=2(d∗

10)

P P V ResultEuclidean P P V ResultM anhattan

0.8382

0.9552

0.9853

0.9403

0.9412

0.9851

0.9559

1.0000

1.0000

1.0000

0.8088

0.9403

0.9853

0.9403

0.9265

0.9851

0.9559

1.0000

1.0000

1.0000

Table 3: V fold cross validation when unknown data is removed

I ran ID3 algorithm with same data set. I used 80% of the data to training set and the rest 20%is used as a test data set. It gave almost 93.3% of accuracy for the same data set.

When we replace the unknown data with mean values, it gave almost same performance as Table1. Table 4 shows the result for PPV values and Table 5 shows the result of V fold cross validation.In the case of V-fold cross validation, the performance was better with the removed data set. Theweighted PPV was 0.9569 for Euclidean distance and 0.9498 for Manhattan distance. In this caseCkm=2(∆∗) P P VEuclidean P P VM anhattan

A1, . . . , A9

A1, . . . , A7

A1, . . . , A5

A1, . . . , A3

0.9571

0.9571

0.9385

0.9385

0.8612

0.9428

0.9528

0.9313

0.9342

0.8612

A1, A2

Table 4: PPV values when unknown data is replaced by mean

Train

Test

Ckm=2(D∗ − d∗

Ckm=2(d∗

1)

1)

Ckm=2(D∗ − d∗

Ckm=2(d∗

2)

2)

Ckm=2(D∗ − d∗

Ckm=2(d∗

3)

3)

Ckm=2(D∗ − d∗

Ckm=2(d∗

4)

4)

Ckm=2(D∗ − d∗

Ckm=2(d∗

5)

5)

Ckm=2(D∗ − d∗

Ckm=2(d∗

6)

6)

Ckm=2(D∗ − d∗

Ckm=2(d∗

7)

7)

Ckm=2(D∗ − d∗

Ckm=2(d∗

8)

8)

Ckm=2(D∗ − d∗

Ckm=2(d∗

9)

9)

Ckm=2(D∗ − d∗

10) Ckm=2(d∗

10)

P P V ResultEuclidean P P V ResultM anhattan

0.8143

0.9565

0.9857

0.9420

0.9571

0.9565

0.9714

0.9855

1.0000

1.0000

0.7857

0.9420

0.9857

0.9420

0.9286

0.9565

0.9714

0.9855

1.0000

1.0000

Table 5: V fold cross validation when unknown data is replaced by mean

though the main was 3.5, I took 3. In the ID3 algorithm, I got 93.45% accuracy using this samedata set which is also decreasing. They are replaced by same value which can cause performancedegradation.

When I replaced the unknown data with the median value, the PPV was same as removed dataset. And the V fold cross validation is same as mean value data set. The weighted PPV was0.9569 for Euclidean distance and 0.9498 for Manhattan distance. In both of these cases datawere replaced by same value, that’s why there performance is almost same. In the ID3 algorithm,I got better result with this data set. It gave 93.58% accurate classiﬁcation for the cancer dataset. Table 6 shows the PPV values and table 7 shows the result for V fold cross validation.When I changed the missing values with some random values between 1 to 10. The PPV resultsafter removing attributes and V fold cross validation was almost same as previous cases. Theweighted PPV was 0.9583 for Euclidean distance and 0.9498 for Manhattan distance. In the caseof ID3 the performance degraded but not much. It can classify almost 93.17% of data correctly.Table 8 and 9 shows the result with the random replacing data set.

So, observing the result we can conclude that the performance is almost same whether we keepor remove the tuples with unknown data. Though, there is some variation with the later datasets, but not much. But replacing the data sometimes degrading the performance. So, there is nosuﬃcient signiﬁcance for keeping the unknown data. Which means, we can use removed data setfor our algorithms.

Ckm=2(∆∗) P P VEuclidean P P VM anhattan

A1, . . . , A9

A1, . . . , A7

A1, . . . , A5

A1, . . . , A3

0.9571

0.9571

0.9385

0.9385

0.8584

0.9428

0.9514

0.9285

0.9342

0.8612

A1, A2

Table 6: PPV when unknown data is replaced by median

Train

Test

Ckm=2(D∗ − d∗

Ckm=2(d∗

1)

1)

Ckm=2(D∗ − d∗

Ckm=2(d∗

2)

2)

Ckm=2(D∗ − d∗

Ckm=2(d∗

3)

3)

Ckm=2(D∗ − d∗

Ckm=2(d∗

4)

4)

Ckm=2(D∗ − d∗

Ckm=2(d∗

5)

5)

Ckm=2(D∗ − d∗

Ckm=2(d∗

6)

6)

Ckm=2(D∗ − d∗

Ckm=2(d∗

7)

7)

Ckm=2(D∗ − d∗

Ckm=2(d∗

8)

8)

Ckm=2(D∗ − d∗

Ckm=2(d∗

9)

9)

Ckm=2(D∗ − d∗

10) Ckm=2(d∗

10)

P P V ResultEuclidean P P V ResultM anhattan

0.8143

0.9565

0.9857

0.9420

0.9571

0.9565

0.9714

0.9855

1.0000

1.0000

0.7857

0.9420

0.9857

0.9420

0.9286

0.9565

0.9714

0.9855

1.0000

1.0000

Table 7: V fold cross validation when unknown data is replaced by median

Ckm=2(∆∗) P P VEuclidean P P VM anhattan

A1, . . . , A9

A1, . . . , A7

A1, . . . , A5

A1, . . . , A3

0.9571

0.9585

0.9385

0.9385

0.8040

0.9428

0.9499

0.9285

0.9342

0.8584

A1, A2

Table 8: PPV when unknown data is replaced by random values

Train

Test

Ckm=2(D∗ − d∗

Ckm=2(d∗

1)

1)

Ckm=2(D∗ − d∗

Ckm=2(d∗

2)

2)

Ckm=2(D∗ − d∗

Ckm=2(d∗

3)

3)

Ckm=2(D∗ − d∗

Ckm=2(d∗

4)

4)

Ckm=2(D∗ − d∗

Ckm=2(d∗

5)

5)

Ckm=2(D∗ − d∗

Ckm=2(d∗

6)

6)

Ckm=2(D∗ − d∗

Ckm=2(d∗

7)

7)

Ckm=2(D∗ − d∗

Ckm=2(d∗

8)

8)

Ckm=2(D∗ − d∗

Ckm=2(d∗

9)

9)

Ckm=2(D∗ − d∗

10) Ckm=2(d10∗)

P P V ResultEuclidean P P V ResultM anhattan

0.8286

0.9565

0.9857

0.9420

0.9571

0.9565

0.9714

0.9855

1.0000

1.0000

0.7857

0.9420

0.9714

0.9420

0.9286

0.9710

0.9714

0.9855

1.0000

1.0000

Table 9: V fold cross validation when unknown data is replaced by random values

- 4.

(a) A6 or Bare Nuclei has the greatest variance. The following code give us the code for calculatingvariance and return the maximum variance at the end of the code.

name<- names(A_i)

datalist <- A_i[,name]

nums<- sapply(A_i,as.numeric)

mean<-mean(nums)

squared_sum <- 0

length_Ai <- length(A_i[,name])

for(i in 1:length_Ai)

{

> calc_variance <- function(A_i)

+ {

+

+

+

+

+

+

+

+

+

+

+

+ }

}

return(squared_sum/(length_Ai-1))

squared_sum <- squared_sum + ((datalist[i]-mean)^2)

maximum_variance <- 0

index <- 0

for(i in 2:10)

{

> get_maxm_variance <- function(dataset)

+ {

+

+

+

+

+

+

+

+

+

+

+

+

+ }

> cleaned_data <- read.table("cancer_data_after_cleaning.csv", header=TRUE, sep=",")> maximum_attr <- get_maxm_variance(cleaned_data)

> print(paste("A_",maximum_attr-1))

variance <- calc_variance(dataset[i])

if(maximum_variance < variance)

{

maximum_variance <- variance

index <- i

}

}

return(index)

[1] "A_ 6"

> print(paste("Feature:",names(cleaned_data[maximum_attr])))

[1] "Feature: Bare.Nuclei"

(b) A6 or Bare Nuclei has the lowest entropy. The following code returns the attribute with lowestentropy.

> install.packages("entropy")

> library("entropy")

> get_minimum_entropy <- function(dataset)

+ {

+

+

+

+

+

minimum_entropy <- 10000000000

index <- 0

for(i in 2:10)

{

data <- dataset[i]

name<- names(data)

datalist <- data[,name]

entropy <- entropy(datalist,unit="log2")

if(minimum_entropy > entropy)

{

+

+

+

+

+

+

+

+

+

+

+ }

> cleaned_data <- read.table("cancer_data_after_cleaning.csv", header=TRUE, sep=",")> min_entrpy <- get_minimum_entropy(cleaned_data)

> print(paste("A_",min_entrpy-1))

minimum_entropy <- entropy

index <- i

}

}

return(index)

[1] "A_ 6"

> print(paste("Feature:",names(cleaned_data[min_entropy])))

[1] "Feature: Bare.Nuclei"

(c) The following code gives us the KL distance for attribute pairs. I use seewave package for calcu-lating the KL distance for each pair of attributes.

> install.packages("seewave")

> library("seewave")

> get_KL_distance <- function(dataset)

+ {

+ name_list <- c("A_1","A_2","A_3","A_4","A_5","A_6", "A_7", "A_8", "A_9")+ kl_matrix <- matrix(1:81, ncol=9, nrow=9, dimnames=list(name_list,name_list))+ for(i in 2:10)

+ {

+ for(j in i:10)

+ {

+ if(i==j)

+ {

+ kl_matrix[i-1,j-1] <- 0

+ }

+ else

+ {

+ kl_dist <- kl.dist(dataset[i],dataset[j])

+ kl_matrix[i-1,j-1] <- round( kl_dist$D1,digits=2)

+ kl_matrix[j-1,i-1] <- round(kl_dist$D2, digits=2)

+ }

+ }

+ }

+ return(kl_matrix)

+ }

> cleaned_data <- read.table("cancer_data_after_pruning.csv", header=TRUE, sep=",")> kl_matrix <- get_KL_distance(cleaned_data)

> install.packages("xtable")

> library("xtable")

> x=xtable(kl_matrix,align=rep("",ncol(kl_matrix)+1))

> print(x, floating=FALSE,

+ hline.after=NULL, include.rownames=TRUE, include.colnames=TRUE)

A1

0.00

0.33

0.31

0.45

0.28

0.44

0.30

0.45

0.49

A2

0.35

0.00

0.09

0.27

0.24

0.30

0.24

0.27

0.48

A3

0.32

0.09

0.00

0.29

0.25

0.29

0.25

0.27

0.50

A4

0.46

0.29

0.31

0.00

0.35

0.38

0.31

0.43

0.55

A5

0.26

0.23

0.24

0.33

0.00

0.40

0.21

0.33

0.34

A6

0.48

0.34

0.33

0.38

0.44

0.00

0.38

0.47

0.68

A7

0.29

0.24

0.24

0.31

0.22

0.34

0.00

0.32

0.49

A8

0.48

0.32

0.33

0.44

0.36

0.50

0.36

0.00

0.54

A9

0.43

0.53

0.53

0.56

0.34

0.68

0.46

0.59

0.00

A1

A2

A3

A4

A5

A6

A7

A8

A9

- 5. The following table is generated after running the k-means algorithm with the cleaned data set. There

was 675 data in total, 439 of them are benign and 236 of them are malignant data. k-means performsbetter with large number of attributes.

It performed better with 9 and 7 attributes. Using 5 and3 attributes degrade the performance but not signiﬁcantly. But using 2 attributes is aﬀecting theperformance of k-means algorithm signiﬁcantly. And after running several iteration, I have come tothis conclusion that using 3,5,7 and 9 attributes gives almost same results in every cases. But usingtwo attribute is completely depends on the quality of the initial centroids.Sometimes it gives less than70% of PPV and sometimes it is greater than 80%. In the case of distance matrix, Euclidean matrixgives slightly better result than Manhattan distance which is obvious. In the ID3 algorithm, I haveimplemented it by using information gain. And it gave approximately 93.3% of accuracy. The accuracywas calculated by calculating true positive, true negative, false positive and false negative.

Ckm=2(∆∗) P P VEuclidean P P VM anhattan

A1, . . . , A9

A1, . . . , A7

A1, . . . , A5

A1, . . . , A3

0.9600

0.9600

0.9437

0.9407

0.8593

0.9467

0.9556

0.9333

0.9363

0.8252

A1, A2

Table 10: PPV result

- 6. After running 10 fold cross validation, I came to this conclusion that removing the ﬁrst 675

tuples gives poor result than removing others. And removing the last 3 partitions does not aﬀect theresult and gives the best result. The weighted PPV was 0.9601 for Euclidean distance and 0.9527 forManhattan distance.

10 ≈ 68Train

Test

Ckm=2(D∗ − d∗

Ckm=2(d∗

1)

1)

Ckm=2(D∗ − d∗

Ckm=2(d∗

2)

2)

Ckm=2(D∗ − d∗

Ckm=2(d∗

3)

3)

Ckm=2(D∗ − d∗

Ckm=2(d∗

4)

4)

Ckm=2(D∗ − d∗

Ckm=2(d∗

5)

5)

Ckm=2(D∗ − d∗

Ckm=2(d∗

6)

6)

Ckm=2(D∗ − d∗

Ckm=2(d∗

7)

7)

Ckm=2(D∗ − d∗

Ckm=2(d∗

8)

8)

Ckm=2(D∗ − d∗

Ckm=2(d∗

9)

9)

Ckm=2(D∗ − d∗

10) Ckm=2(d∗

10)

P P V ResultEuclidean P P V ResultM anhattan

0.8382

0.9552

0.9853

0.9403

0.9412

0.9851

0.9559

1.0000

1.0000

1.0000

0.8088

0.9403

0.9853

0.9403

0.9265

0.9851

0.9559

1.0000

1.0000

1.0000

Table 11: V fold Cross Validation

- Problem Three: Astronomy

- 1. R(g, g(cid:48)) is a metric. The proof is given below-

Proof of Positivity: R(g, g(cid:48)) ≥ 0 for all g and g(cid:48). It is trivial because -

(cid:32)(cid:18) zg − zg(cid:48)

(cid:19)2

(cid:18) rg − rg(cid:48)

(cid:19)2

(cid:18) (ug − rg) − (ug(cid:48) − rg(cid:48))

(cid:19)2(cid:33) 1

R(g, g(cid:48)) =

0.2596

8.6

+

+

11.84

is constituted by adding three square terms. The square of any value is always greater than 0. So, Rwill also be positive. And, R is constructed by three terms. Each term has pair of diﬀerences betweentwo rows. So, each term will be equal to zero when the values are equal. For example, when zg = z(cid:48)the ﬁrst term will be equal to zero. This is same for the rest of the terms. So R = 0, when g = g(cid:48). So,this proves the proof of positivity.

Proof of Symmetry: This is also a trivial proof.

gR(g, g(cid:48)) =

(cid:32)(cid:18) zg − zg(cid:48)

(cid:32)(cid:18) zg(cid:48) − zg

0.2596

(cid:19)2

(cid:19)2

(cid:18) rg − rg(cid:48)

(cid:18) rg(cid:48) − rg

8.6

(cid:19)2

(cid:19)2

8.6

+

+

+

+

(cid:18) (ug − rg) − (ug(cid:48) − rg(cid:48))

(cid:18) (ug(cid:48) − rg(cid:48)) − (ug − rg)

11.84

(cid:19)2(cid:33) 1

(cid:19)2(cid:33) 1

11.84

=

= R(g(cid:48), g)

0.2596

[(a − b)2 = (b − a)2]So, R follows the proof of symmetry.

Proof of Triangle Inequality: To proof the triangle inequality, we can rewrite R in following way-R(g, g(cid:48)) = (cid:112)(a1 − b1)2 + (a2 − b2)2 + (a3 − b3)2

(cid:118)(cid:117)(cid:117)(cid:116)(cid:32) 3(cid:88)

=

(a − b)2

(cid:33)

Where,

i=1

= d(a, b)

a1 =

b1 =

a2 =

b2 =

a3 =

0.2596

zg

z(cid:48)

g

0.2596

rg

8.6

z(cid:48)

g

8.6

ug − rg

11.84

g − r(cid:48)

u(cid:48)

g

11.84

b3 =

a = a1, a2, a3

b = b1, b2, b3

So, we can prove the triangle inequality by proving the follownig equation

d(a, c) ≤ d(a, b) + d(b, c)

[d(a, b) =

(a − b)2

(cid:118)(cid:117)(cid:117)(cid:116)(cid:32) 3(cid:88)

(cid:33)

]

i=1

If we consider a and b as a vector then d(a, b) = d((cid:126)a,(cid:126)b) = |(cid:126)a − (cid:126)b|. So,

d(a, c) = d((cid:126)a − (cid:126)c) = |(cid:126)a − (cid:126)c|

= |(cid:126)a − (cid:126)b + (cid:126)b − (cid:126)c|

≤ |(cid:126)a − (cid:126)b| + |(cid:126)b − (cid:126)c|

≤ d(a, b) + d(b, c)

[|x + y| ≤ |x| + |y|]

So, triangle inequality holds for R(g, g(cid:48)). All the three conditions holds for R, so R is a metric.- 2. I ran the control algorithm and get the control list which consists 7246 rows. I compared it with the

control 2 subset and got 1093 matching instances. I ran it several times and got almost same numberof matching values. I compared the Galaxy Id of control sets with the Galaxy Id of the control 2 sets. Istored my control values in the control.csv ﬁle which may be found in the DMHW P3 directory. And istored the matched instances in the matched.csv ﬁle. The Galaxy Ids can be found in controloutput.txtﬁle. Which may not same as the actual Id’s. But, the actual Id’s can be found in matched.csv ﬁle. Atﬁrst, the algorithm took almost 15 minutes but after dividing it into threads it gives the result with in5 minutes.

- 3. I ran the k-means algorithm on candidate data set using main as centroids. I divided the candidate

data set into three other data set which is candidate 0, candidate 1 and candidate 2 for galaxy types0,1 and 2. Similarly, I divided the main data set into three other data set main 0 , main 1 and main 2.They stored into csv ﬁle. Then I ran the k-means algorithm on these data set. After running k-means algorithm on these data set , I got three blocks of clusters and the centroids were stored incluster 0 block.csv, cluster 1 block.csv and cluster 2 block.csv. In those ﬁles I stored their values andI added one extra column which shows data count. Data count is actually showing number of candidatedata associated with the corresponding clusters. The runtime for block 0 and block 1 is pretty small.But, it took almost 6 hours to run the algorithm for block 2.

After running the algorithm several times, I found out that the speed of convergence is dependent onthe threshold value. I set 5,2 and 1 as threshold margin. If I use threshold margin 5 for block 0, thealgorithm gives the ﬁnal centroids after 28 iterations. For threshold margin 2, it gives the ﬁnal resultafter 44 iterations. And for threshold margin 1, it took 47 iterations. For block 1, if I use thresholdmargin 5 it needs 94 iterations to give the ﬁnal centroids. For threshold margin 2, it took 112 iterationsand for threshold margin 1, it took 141 iterations. If i use threshold margin 5, it needs almost 284steps to get the ﬁnal centroids for block 2.

- 4. I have run some R codes to perform data analysis on the features of main and candidate. For the

candidate data, the galaxy type has three diﬀerent values 0,1,2. There are 12355 instances with galaxytype 0, 51247 instances with galaxy type 1, 697905 instances with galaxy type 2 values. For the restof the attributes the mean, median, maximum and minimum values are listed below-

Attribute name

right ascension

declination

u-band magnitude

r-band magnitude

- the spectroscopic redshift

Max Value Min Value

359.9979

84.270

33.05

20.792

20.792

0.0007

-14.071

-9999.00

-1.385

-0.01011

Mean

182.4604

23.918

19.47

17.091

0.12048

Median

184.3858

22.444

19.57

17.244

0.10756

Variance

3852.645

375.7885

1451.558

0.7836468

0.00512957

Entropy13.4741413.3705313.6338713.5416913.37489Table 12: Summary of candidate data

Table 13 represents the maximum value, minimum value, mean, median, variance and entropy for maindata. There are 7246 rows in the main data set, in which 949 of them have galaxy type 0, 859 havegalaxy type 1 and 5438 have galaxy type 2. Both table 12 and 13 are generated by using some basicR functions like summary, var and entropy.

Attribute name

right ascension

declination

u-band magnitude

r-band magnitude

- the spectroscopic redshift

64.43

28.85

20.79

0.29955

Max Value Min Value

359.9950

0.0065

-11.19

15.08

12.19

0.01252

Mean

184.5384

Median

184.9200

25.36

19.87

16.67

0.16303

24.16

20.00

16.87

0.16466

Variance

2828.193

344.3973

1.330857

0.7004889

0.003377972

Entropy8.8401688.68238.8865198.8869258.821028Table 13: Summary of main data

If we look at both of the tables we can see that for all of the attributes the values are almost same.But there is signiﬁcant discrepancy for the u-band magnitude attribute in the candidate data set. Theminimum value for u-band magnitude in the candidate data set is signiﬁcantly small considering otherattributes. And, the minimum value for u-band magnitude in the main data set is 15.08. The diﬀerenceis quite large. And the diﬀerence of these values is also causing higher diﬀerence in their variance. Inthe main data set, the variance is near to 1, whether the variance for u-band magnitude in candidatedata set is over 1450. Which is reasonably large. So, I wrote another R code to search for counting thenegative values in the u-band magnitude attribute on candidate data set. And found that there are 12rows which have negative values for the u-band magnitude attribute. After removing them I got theminimum value for u-band magnitude is 5.40 and the variance becomes 1.6888. Now, the values of twotables are pretty closed. There might be some discrepancy but that could be negligible considering thediﬀerence of the data set.

