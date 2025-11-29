# AssistantCoach
 --- Harokopio University ---

## 📦 Project Structure

```
src/
 └── main/
      └── java/
           └── org/huacoach/
                ├── app/
                │    ├── Main.java
                │    └── CommandLineRunner.java
                │
                ├── interfaces/
                │    └── Activity.java
                │
                ├── model/
                │    ├── activity/
                │    │    ├── AbstractActivity.java
                │    │    ├── RunningActivity.java
                │    │    ├── CyclingActivity.java
                │    │    ├── SwimmingActivity.java
                │    │    ├── WalkingActivity.java
                │    │    ├── Lap.java
                │    │    ├── Track.java
                │    │    └── TrackPoint.java
                │    │
                │    └── profile/
                │         └── UserProfile.java
                │
                ├── parser/
                │    └── TcxFileParser.java
                │
                ├── services/
                │    ├── ActivityService.java
                │    ├── CaloriesCalculator.java
                │    └── StatisticsCalculator.java
                │
                └── cli/
                     ├── ArgumentParser.java
                     └── ConsolePrinter.java
```
