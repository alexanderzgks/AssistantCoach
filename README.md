# AssistantCoach
 --- Harokopio University ---

 ## Project Description
Το AssistantCoach είναι μια Java εφαρμογή που αναλύει αρχεία
δραστηριοτήτων (.tcx) και υπολογίζει στατιστικά στοιχεία προπόνησης, 
όπως θερμίδες και μέσες τιμές.

## Students
- it2023017 - Ζόγκας Αλέξανδρος 
- it2023053 - Ξυπολίας Κωσταντίνος 
- it2023131 - Ζακέος Μάρκος Νικόλαος

## 📦 Project Structure

```
org.huacoach
├── app
│   └── Main.java
│
├── enums
│   ├── Sex.java
│   └── SportType.java
│
├── model
│   ├── Lap.java
│   ├── Track.java
│   ├── Trackpoint.java
│   └── XMLActivity.java
│
├── parser
│   └── TcxFileParser.java
│
├── services
│   ├── ActivityPrinter.java
│   ├── ActivityService.java
│   ├── CaloriesCalculator.java
│   └── StatisticsCalculator.java
│
└── validation
    └── ArgsValidator.java
```

## How It Works
1. Γίνεται έλεγχος ορισμάτων εισόδου
2. Διαβάζονται τα αρχεία TCX
3. Δημιουργούνται αντικείμενα Activity / Lap / Trackpoint / Track.
4. Υπολογίζονται στατιστικά
5. Εκτυπώνονται τα αποτελέσματα

## Explain the Structure and Classes
### org.huacoach.app
Ρόλος: το “entry point” της εφαρμογής (CLI).
 ΜΑΙΝ
 - Ελέγχει τα arguments της γραμμής εντολών μέσω ArgsValidator.
 - Διαβάζει τα αρχεία TCX που δόθηκαν, χρησιμοποιώντας τον TcxFileParser.
 - Αποθηκεύει τις έγκυρες δραστηριότητες (XMLActivity) σε λίστα.
 - Αν δεν βρεθεί καμία έγκυρη δραστηριότητα, τερματίζει.
### org.huacoach.enums
Ρόλος: προσφαίρει σταθερές τιμές
### org.huacoach.model
Ρόλος: τα “data objects” (οντότητες) που αναπαριστούν το περιεχόμενο του TCX.
  XMLActivity
  - Η XMLActivity είναι ένα data object που αναπαριστά μία αθλητική δραστηριότητα από αρχείο TCX και αποθηκεύει τον τύπο αθλήματος, τον χρόνο έναρξης       και τα laps της δραστηριότητας.


Για κάθε δραστηριότητα, εκτυπώνει τα αποτελέσματα μέσω ActivityPrinter, λαμβάνοντας υπόψη και το βάρος (αν δόθηκε).


## Instructions
Για να τρέξετε το πρόγραμμα θα πρέπει να να μεταβείται στον φάκελο του προγράμματος και έπειτα θα πρέπει
να πάτε στο out/artifacts/AssistantCoach_jar (στον οποίο φάκελο υπάρχουν δυο αρχεία tcx) και επείτα μπορείτε να τρέξετε τις εξής εντολές:

```
out/
└── artifacts/
    └── AssistantCoach_jar/
        ├── activity.tcx
        ├── activityTwo.tcx
        └── AssistantCoach.jar
```

 - java -jar AssistantCoach.jar activity.tcx activityTwo.tcx
 - java -jar AssistantCoach.jar -w 65.9 activity.tcx activityTwo.tcx
