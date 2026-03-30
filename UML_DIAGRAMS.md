# Sims Game - UML Class Diagrams

This document contains comprehensive UML class diagrams for the text-based Sims game, showing all classes and their relationships.

---

## 1. Complete System Overview

```mermaid
classDiagram
    %% Main Classes
    class Main {
        +main(String[] args)$ void
    }

    class ConsoleUI {
        -GameEngine engine
        -Scanner sc
        +ConsoleUI(GameEngine engine)
        +run() void
    }

    class GameEngine {
        -List~SimCharacter~ sims
        -int activeSimIndex
        -List~Child~ pendingNewborns
        -Time time
        -ActivityRegistry activityRegistry
        +startNewGame() void
        +createSim(String name, String gender) void
        +performActivity(int activityIndex) String
        +advanceTime(int hours) void
        +isGameOver() boolean
    }

    %% Interfaces
    class GameContext {
        <<interface>>
        +advanceTime(int hours) void
        +getCurrentDay() int
        +getCurrentHour() int
        +notify(String message) void
    }

    %% Engine Components
    class Time {
        -int currentDay
        -int currentHour
        +reset() void
        +advance(int hours) int
        +getCurrentDay() int
        +getCurrentHour() int
    }

    class ActivityRegistry {
        -List~Activity~ activities
        +getAllActivities() List~Activity~
        +getActivityByIndex(int index) Activity
    }

    %% Sim Character and Components
    class SimCharacter {
        -String name
        -int age
        -String gender
        -int hungerZeroDay
        -int energyZeroDay
        -boolean alive
        -boolean busy
        -int remainingBusyHours
        -Needs needs
        -Mood mood
        -Wallet wallet
        -Family family
        -Career career
        +perform(Activity activity, GameContext context) void
        +checkDeath(GameContext context) void
        +assignBusyTime(int hours) void
        +reduceBusyTime(int hours) void
    }

    class Needs {
        -int hunger
        -int energy
        -int hygiene
        -int social
        -int fun
        +addHunger(int value) void
        +addEnergy(int value) void
        +addHygiene(int value) void
        +addSocial(int value) void
        +addFun(int value) void
        +getAvgStats() double
        +isCritical() boolean
        +decay(int hours) void
    }

    class Mood {
        -moodState currentMood
        +update(Needs needs) void
        +getMoodName() moodState
    }

    class moodState {
        <<enumeration>>
        HAPPY
        OKAY
        STRESSED
        MISERABLE
    }

    class Wallet {
        -double balance
        +earn(double amount) void
        +spend(double amount) boolean
        +getBalance() double
    }

    class Family {
        -List~Child~ children
        -boolean pregnant
        -int pregnancyDay
        -int PREGNANCY_DURATION$
        +tryForBaby(String name, int currentDay) String
        +onNewDay(int currentDay) List~Child~
        +careForChildren() void
        +getGrownUpChildren() List~Child~
        +getNeglectedChildren() List~Child~
        +removeChild(Child child) void
    }

    class Child {
        -String name
        -int ageInDays
        -String gender
        -int birthDay
        -boolean needCare
        -int daysWithoutCare
        +setName(String name) void
        +updateAge(int currentDay) void
        +updateCareStatus(boolean care) void
        +isGrownUp() boolean
        +isNeglectedToDeath() boolean
    }

    %% Activity Classes
    class Activity {
        <<interface>>
        +name() String
        +hours() int
        +apply(SimCharacter sim, GameContext context) void
    }

    class AbstractActivity {
        <<abstract>>
        -String name
        -int hours
        #AbstractActivity(String name, int hours)
        +name() String
        +hours() int
    }

    class EatActivity {
        -int mealCost
        -int hungerGain
        +apply(SimCharacter sim, GameContext context) void
    }

    class SleepActivity {
        -int energyGain
        +apply(SimCharacter sim, GameContext context) void
    }

    class ShowerActivity {
        -int hygieneGain
        +apply(SimCharacter sim, GameContext context) void
    }

    class SocialiseActivity {
        -int socialGain
        -int funGain
        +apply(SimCharacter sim, GameContext context) void
    }

    class PlayActivity {
        -int funGain
        -int energyLoss
        -int cost
        +apply(SimCharacter sim, GameContext context) void
    }

    class WorkActivity {
        -Scanner scanner
        +name() String
        +hours() int
        +apply(SimCharacter sim, GameContext context) void
    }

    %% Career Classes
    class Career {
        <<abstract>>
        -CareerPromotion promotion
        -int level
        -int workExperience
        +work(SimCharacter sim, GameContext context, int hours) double
        #checkPromotion() void
        +getWorkHours(SimCharacter sim)* int
        #calculatePay(SimCharacter sim, int hours)* double
        +getCareerName() String
        +getCurrentTitle() String
    }

    class ShiftJob {
        +getWorkHours(SimCharacter sim) int
        #calculatePay(SimCharacter sim, int hours) double
    }

    class FixedTimeJob {
        -int fixedHours
        +getWorkHours(SimCharacter sim) int
        #calculatePay(SimCharacter sim, int hours) double
    }

    class CommissionJob {
        -double basePay
        -double commissionRate
        -int commissionHours
        -Random bonusPay
        +getWorkHours(SimCharacter sim) int
        #calculatePay(SimCharacter sim, int hours) double
    }

    class CareerPromotion {
        -String careerName
        -String[] titles
        -double[] payRates
        -int[] reqs
        +getTitle(int level) String
        +getPayRate(int level) double
        +getPromotionRequirement(int level) int
    }

    class CareerFactory {
        <<utility>>
        +createCareer(int type, String customTitle)$ Career
        +getCareerTypes()$ List~String~
    }

    %% Relationships - Main Structure
    Main --> ConsoleUI : creates
    Main --> GameEngine : creates
    ConsoleUI --> GameEngine : uses
    GameEngine ..|> GameContext : implements
    GameEngine *-- Time : composition
    GameEngine *-- ActivityRegistry : composition
    GameEngine o-- SimCharacter : aggregation
    GameEngine o-- Child : manages pending

    %% SimCharacter Relationships
    SimCharacter *-- Needs : composition
    SimCharacter *-- Mood : composition
    SimCharacter *-- Wallet : composition
    SimCharacter *-- Family : composition
    SimCharacter o-- Career : aggregation

    %% Mood Relationships
    Mood *-- moodState : contains
    Mood ..> Needs : depends on

    %% Family Relationships
    Family *-- Child : composition

    %% Activity Relationships
    ActivityRegistry o-- Activity : contains
    Activity <|.. AbstractActivity : implements
    Activity <|.. WorkActivity : implements
    AbstractActivity <|-- EatActivity : extends
    AbstractActivity <|-- SleepActivity : extends
    AbstractActivity <|-- ShowerActivity : extends
    AbstractActivity <|-- SocialiseActivity : extends
    AbstractActivity <|-- PlayActivity : extends

    Activity ..> SimCharacter : uses
    Activity ..> GameContext : uses

    %% Career Relationships
    Career <|-- ShiftJob : extends
    Career <|-- FixedTimeJob : extends
    Career <|-- CommissionJob : extends
    Career *-- CareerPromotion : composition
    CareerFactory ..> Career : creates
    CareerFactory ..> CareerPromotion : creates
```

---

## 2. Core Game Engine Package

```mermaid
classDiagram
    class GameEngine {
        -List~SimCharacter~ sims
        -int activeSimIndex
        -List~Child~ pendingNewborns
        -Time time
        -ActivityRegistry activityRegistry
        +GameEngine()
        +startNewGame() void
        +createSim(String name, String gender) void
        +getSimCount() int
        +getSimNames() List~String~
        +getActiveSimIndex() int
        +setActiveSimIndex(int index) void
        +getActiveSim() SimCharacter
        +getActiveSimStatus() String
        +getActivityNames() List~String~
        +performActivity(int activityIndex) String
        +getCareerOptions() List~String~
        +assignCareerToActiveSim(int type, String title) String
        +tryForBabyActiveSim() String
        +careForChildrenActiveSim() String
        +advanceTime(int hours) void
        +allLivingSimsBusy() boolean
        +advanceUntilNextAvailableSim() void
        +rotateToNextAvailableSim() void
        +isGameOver() boolean
    }

    class GameContext {
        <<interface>>
        +advanceTime(int hours) void
        +getCurrentDay() int
        +getCurrentHour() int
        +notify(String message) void
    }

    class Time {
        -int currentDay
        -int currentHour
        +Time()
        +reset() void
        +advance(int hours) int
        +getCurrentDay() int
        +getCurrentHour() int
    }

    class ActivityRegistry {
        -List~Activity~ activities
        +ActivityRegistry()
        +getAllActivities() List~Activity~
        +getActivityByIndex(int index) Activity
    }

    GameEngine ..|> GameContext : implements
    GameEngine *-- "1" Time : composition
    GameEngine *-- "1" ActivityRegistry : composition
    GameEngine o-- "*" SimCharacter : manages
    GameEngine ..> Activity : uses
    GameEngine ..> Career : uses
    GameEngine ..> Child : manages pending
    ActivityRegistry o-- "*" Activity : contains
```

---

## 3. SimCharacter and Components Package

```mermaid
classDiagram
    class SimCharacter {
        -String name
        -int age
        -String gender
        -int hungerZeroDay
        -int energyZeroDay
        -boolean alive
        -boolean busy
        -int remainingBusyHours
        -Needs needs
        -Mood mood
        -Wallet wallet
        -Family family
        -Career career
        +SimCharacter(String name, String gender)
        +getName() String
        +getGender() String
        +getNeeds() Needs
        +getMood() Mood
        +getWallet() Wallet
        +getFamily() Family
        +getCareer() Career
        +isAlive() boolean
        +isBusy() boolean
        +getRemainingBusyHours() int
        +perform(Activity activity, GameContext context) void
        +checkDeath(GameContext context) void
        +assignBusyTime(int hours) void
        +reduceBusyTime(int hours) void
        +setCareer(Career career) void
        +removeCareer() void
        +getStatusString() String
    }

    class Needs {
        -int hunger
        -int energy
        -int hygiene
        -int social
        -int fun
        +Needs()
        +getHunger() int
        +getEnergy() int
        +getHygiene() int
        +getSocial() int
        +getFun() int
        +addHunger(int value) void
        +addEnergy(int value) void
        +addHygiene(int value) void
        +addSocial(int value) void
        +addFun(int value) void
        +getAvgStats() double
        +getStatsStr() String
        +isCritical() boolean
        +decay(int hours) void
        -clamp(int value) int
    }

    class Mood {
        -moodState currentMood
        +Mood()
        +getMoodName() moodState
        +update(Needs needs) void
    }

    class moodState {
        <<enumeration>>
        HAPPY
        OKAY
        STRESSED
        MISERABLE
    }

    class Wallet {
        -double balance
        +Wallet(double amount)
        +getBalance() double
        +earn(double amount) void
        +spend(double amount) boolean
    }

    class Family {
        -List~Child~ children
        -boolean pregnant
        -int pregnancyDay
        -int PREGNANCY_DURATION$
        +Family()
        +getChildren() List~Child~
        +isPregnant() boolean
        +tryForBaby(String name, int currentDay) String
        +onNewDay(int currentDay) List~Child~
        +careForChildren() void
        +getGrownUpChildren() List~Child~
        +getNeglectedChildren() List~Child~
        +removeChild(Child child) void
        -giveBirth(int currentDay) Child
    }

    class Child {
        -String name
        -int ageInDays
        -String gender
        -int birthDay
        -boolean needCare
        -int daysWithoutCare
        +Child(String name, String gender, int currentDay)
        +getName() String
        +getAgeInDays() int
        +getGender() String
        +getBirthDay() int
        +getCareStatus() boolean
        +getDaysWithoutCare() int
        +setName(String name) void
        +updateAge(int currentDay) void
        +updateCareStatus(boolean care) void
        +isGrownUp() boolean
        +isNeglectedToDeath() boolean
        +getInfo() String
    }

    SimCharacter *-- "1" Needs : composition
    SimCharacter *-- "1" Mood : composition
    SimCharacter *-- "1" Wallet : composition
    SimCharacter *-- "1" Family : composition
    SimCharacter o-- "0..1" Career : aggregation

    Mood *-- moodState : contains
    Mood ..> Needs : depends on

    Family *-- "*" Child : composition
```

---

## 4. Activity Package Hierarchy

```mermaid
classDiagram
    class Activity {
        <<interface>>
        +name() String
        +hours() int
        +apply(SimCharacter sim, GameContext context) void
    }

    class AbstractActivity {
        <<abstract>>
        -String name
        -int hours
        #AbstractActivity(String name, int hours)
        +name() String
        +hours() int
        +apply(SimCharacter sim, GameContext context)* void
    }

    class EatActivity {
        -int mealCost
        -int hungerGain
        +EatActivity(int mealCost, int hungerGain, int hours)
        +apply(SimCharacter sim, GameContext context) void
    }

    class SleepActivity {
        -int energyGain
        +SleepActivity(int energyGain, int hours)
        +apply(SimCharacter sim, GameContext context) void
    }

    class ShowerActivity {
        -int hygieneGain
        +ShowerActivity(int hygieneGain, int hours)
        +apply(SimCharacter sim, GameContext context) void
    }

    class SocialiseActivity {
        -int socialGain
        -int funGain
        +SocialiseActivity(int socialGain, int funGain, int hours)
        +apply(SimCharacter sim, GameContext context) void
    }

    class PlayActivity {
        -int funGain
        -int energyLoss
        -int cost
        +PlayActivity(int funGain, int energyLoss, int hours, int cost)
        +apply(SimCharacter sim, GameContext context) void
    }

    class WorkActivity {
        -Scanner scanner
        +WorkActivity()
        +name() String
        +hours() int
        +apply(SimCharacter sim, GameContext context) void
    }

    Activity <|.. AbstractActivity : implements
    Activity <|.. WorkActivity : implements

    AbstractActivity <|-- EatActivity : extends
    AbstractActivity <|-- SleepActivity : extends
    AbstractActivity <|-- ShowerActivity : extends
    AbstractActivity <|-- SocialiseActivity : extends
    AbstractActivity <|-- PlayActivity : extends

    Activity ..> SimCharacter : uses
    Activity ..> GameContext : uses
    WorkActivity ..> Career : uses
```

---

## 5. Career Package Hierarchy

```mermaid
classDiagram
    class Career {
        <<abstract>>
        -CareerPromotion promotion
        -int level
        -int workExperience
        +Career(CareerPromotion promotion)
        +work(SimCharacter sim, GameContext context, int hours) double
        #checkPromotion() void
        +getWorkHours(SimCharacter sim)* int
        #calculatePay(SimCharacter sim, int hours)* double
        #getPromotion() CareerPromotion
        +getLevel() int
        +getWorkExperience() int
        +getCareerName() String
        +getCurrentTitle() String
        +getCurrentPayRate() double
    }

    class ShiftJob {
        +ShiftJob(CareerPromotion promotion)
        +getWorkHours(SimCharacter sim) int
        #calculatePay(SimCharacter sim, int hours) double
    }

    class FixedTimeJob {
        -int fixedHours
        +FixedTimeJob(CareerPromotion promotion, int fixedHours)
        +getWorkHours(SimCharacter sim) int
        #calculatePay(SimCharacter sim, int hours) double
    }

    class CommissionJob {
        -double basePay
        -double commissionRate
        -int commissionHours
        -Random bonusPay
        +CommissionJob(CareerPromotion promotion, double basePay, double commissionRate, int commissionHours)
        +getWorkHours(SimCharacter sim) int
        #calculatePay(SimCharacter sim, int hours) double
    }

    class CareerPromotion {
        -String careerName
        -String[] titles
        -double[] payRates
        -int[] reqs
        +CareerPromotion(String careerName, String[] titles, double[] payRates, int[] reqs)
        +getCareerName() String
        +getTitle(int level) String
        +getPayRate(int level) double
        +getPromotionRequirement(int level) int
    }

    class CareerFactory {
        <<utility>>
        +createCareer(int type, String customTitle)$ Career
        +getCareerTypes()$ List~String~
    }

    Career <|-- ShiftJob : extends
    Career <|-- FixedTimeJob : extends
    Career <|-- CommissionJob : extends

    Career *-- "1" CareerPromotion : composition

    CareerFactory ..> Career : creates
    CareerFactory ..> CareerPromotion : creates
    CareerFactory ..> ShiftJob : creates
    CareerFactory ..> FixedTimeJob : creates
    CareerFactory ..> CommissionJob : creates
```

---

## 6. Relationship Legend and Summary

### Relationship Types Used

| Symbol | Relationship | Description | Example |
|--------|--------------|-------------|---------|
| `*--` | Composition | Strong ownership, part cannot exist without whole | SimCharacter *-- Needs |
| `o--` | Aggregation | Weak ownership, part can exist independently | SimCharacter o-- Career |
| `<|--` | Inheritance | Is-A relationship | EatActivity <\|-- AbstractActivity |
| `<|..` | Implementation | Implements interface | AbstractActivity <\|.. Activity |
| `..>` | Dependency | Uses temporarily | Activity ..> GameContext |
| `-->` | Association | General relationship | Main --> ConsoleUI |

### Key Design Patterns Demonstrated

1. **Factory Pattern**: `CareerFactory` creates different Career types
2. **Strategy Pattern**: `Activity` interface with multiple implementations
3. **Template Method**: `AbstractActivity` provides base implementation
4. **MVC-like**: `GameEngine` (Model), `ConsoleUI` (View/Controller)
5. **Interface Segregation**: `GameContext` provides minimal interface

### Class Count by Package

- **Main**: 2 classes (Main, ConsoleUI)
- **Engine**: 4 classes (GameEngine, GameContext interface, Time, ActivityRegistry)
- **Sims**: 6 classes (SimCharacter, Needs, Mood, moodState enum, Wallet, Family, Child)
- **Activity**: 8 classes (Activity interface, AbstractActivity, 6 concrete activities)
- **Career**: 6 classes (Career abstract, CareerPromotion, CareerFactory, 3 job types)

**Total: 26 classes/interfaces**

### Composition vs Aggregation Examples

**Composition (filled diamond *--):**
- SimCharacter *-- Needs: Needs cannot exist without Sim
- SimCharacter *-- Mood: Mood is part of Sim's identity
- Family *-- Child: Children belong to family unit
- Career *-- CareerPromotion: Promotion structure belongs to career

**Aggregation (hollow diamond o--):**
- SimCharacter o-- Career: Sim can exist without career, career can change
- GameEngine o-- SimCharacter: Sims can exist independently of engine
- ActivityRegistry o-- Activity: Activities exist independently

This demonstrates proper understanding of object ownership and lifecycle management!
