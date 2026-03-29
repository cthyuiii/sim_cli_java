# The Sims - Text Edition 🏠

A text-based life simulation game inspired by The Sims, built with Java as a coursework project for CSC1109 - Object-Oriented Programming at SIT.

## 📋 Table of Contents
- [Game Overview](#game-overview)
- [Features](#features)
- [Installation & Running](#installation--running)
- [How to Play](#how-to-play)
- [Game Mechanics](#game-mechanics)
- [Architecture](#architecture)

---

## 🎮 Game Overview

Manage your Sim's life in this colorful terminal-based simulation! Keep your Sim alive by managing their needs, building a career, and maintaining relationships. Watch out - neglecting your Sim's needs can lead to death from starvation or exhaustion!

**Core Gameplay Loop:**
1. Monitor your Sim's 5 needs (Hunger, Energy, Hygiene, Social, Fun)
2. Perform activities to fulfill needs and advance time
3. Work in a career to earn money
4. Balance needs with career progression
5. Avoid death by keeping Hunger and Energy above 0

---

## ✨ Features

### 🎨 Enhanced UI/UX
- **Color-coded dashboard** - Green (good), Yellow (warning), Red (critical)
- **Progress bars** - Visual representation of all needs
- **Real-time status display** - See all stats before making decisions
- **Smart warnings** - Emergency alerts and recommendations
- **One-press actions** - Quick access to common activities (Eat, Sleep, etc.)

### 🎯 Gameplay Features
- **5 Core Needs System** - Hunger, Energy, Hygiene, Social, Fun
- **Dynamic Mood System** - Mood affects need decay rates
- **Career System** - 3 career types with promotion paths
- **Death Mechanics** - Starvation (Hunger ≤ 0) and Exhaustion (Energy ≤ 0)
- **Family System** - Try for baby and raise children
- **Time Management** - 24-hour day/night cycle

### 🏢 Available Careers
1. **Shift Jobs** (4-8 hours/shift) - Flexible work hours
2. **Fixed Time Jobs** (8 hours/shift) - Regular 9-to-5 schedule
3. **Commission Jobs** (6 hours/shift) - Performance-based pay

Each career has 3 levels with increasing pay and promotion requirements.

---

## 🚀 Installation & Running

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Terminal/Command Prompt with UTF-8 encoding support

### Compilation

Navigate to the `src` directory and compile all files:

```bash
cd src
javac -encoding UTF-8 simsgame/*.java
```

Or compile individually by package:

```bash
javac simsgame/*.java
javac simsgame/engine/*.java
javac simsgame/sims/*.java
javac simsgame/activity/*.java
javac simsgame/career/*.java
```

### Running the Game

From the `src` directory:

```bash
# Run this first if you are on Windows OS.
# To ensure CLI is using UTF-8 Encoding
chcp 65001
```

```bash
java simsgame.Main
```

### Troubleshooting

**Emojis not displaying correctly?**
- Ensure your terminal supports UTF-8 encoding
- macOS Terminal: Should work out of the box
- Windows CMD: Use Windows Terminal or enable UTF-8 mode
- Linux: Most modern terminals support UTF-8

**Colors not showing?**
- Ensure your terminal supports ANSI color codes
- Windows users: Use Windows Terminal (not classic CMD)

---

## 🎮 How to Play

### First Launch

1. **Create Your Sim**
   - Press `9` to access Sim Management
   - Enter your Sim's name
   - Your Sim starts with:
     - $100.00 starting money
     - All needs at 80 (Good condition)
     - Age 18, unemployed

2. **The Dashboard**
   - Top section: Name, Money, Career, Mood, Day/Time
   - Middle section: 5 needs with color-coded progress bars
   - Bottom section: 6 quick action buttons

### Quick Actions (Main Gameplay)

Press the number to perform an action immediately:

1. **🍖 Eat (1h)** - Costs $10.00, +25 Hunger
2. **⚡ Sleep (8h)** - Free, +40 Energy
3. **🚿 Shower (1h)** - Free, +30 Hygiene
4. **👥 Social (2h)** - Free, +25 Social, +10 Fun
5. **🎮 Play (2h)** - Costs $5.00, +25 Fun, -10 Energy
6. **💼 Work** - Hours vary by career, earn money

### Other Options

7. **💼 Career Management**
   - Assign a new career
   - View current career status
   - Track promotions (auto-promoted after earning experience)

8. **👶 Family Options**
   - Try for baby (requires good needs)
   - View family status

9. **👤 Manage Sims**
   - Create new Sims
   - Switch between Sims
   - See which Sims are alive/deceased

&emsp;&ensp;0. **🚪 Quit Game**

---

## 🧠 Game Mechanics

### Needs System

All needs range from 0-100:
- **80-100** 🟢 Excellent (Green)
- **60-79** 🟢 Good (Green)
- **40-59** 🟡 Okay (Yellow)
- **20-39** 🟡 Low (Yellow)
- **0-19** 🔴 CRITICAL (Red)

**Need Decay:**
- All needs decay over time as you perform activities
- Mood affects decay rates (Happy = slower decay)
- Each activity advances time, causing passive decay

### Mood System

Your Sim's mood is calculated from average needs:
- 😊 **HAPPY** - Average needs > 60 (Green)
- 😐 **OKAY** - Average needs 40-60 (Yellow)
- 😰 **STRESSED** - Average needs < 40 (Magenta)
- 😱 **MISERABLE** - Any need ≤ 10 (Red)

### Death Mechanics ☠️

**Your Sim can die from:**

1. **Starvation**
   - Hunger reaches 0 and stays at 0 for 1 full day
   - Warning: Feed your Sim when Hunger drops below 30!

2. **Exhaustion**
   - Energy reaches 0 and stays at 0 for 1 full day
   - Warning: Let your Sim sleep when Energy drops below 30!

**When a Sim dies:**
- 💀 Death screen displays with final needs
- Cannot perform any actions
- Must switch to another Sim or create a new one
- Death is permanent!

### Money Management

**Earning Money:**
- Work in a career (press `6`)
- Pay varies by career type and level
- Promotions increase pay rate

**Spending Money:**
- Eating costs $10.00
- Playing costs $5.00
- Other activities are free

**Going Broke:**
- Can't eat or play if you don't have enough money
- Actions become grayed out
- Must work to earn money!

### Time System

- Each activity takes time (shown in hours)
- Time advances: Day 1, Hour 0 → Day 1, Hour 8 (after 8h sleep)
- 24-hour cycle (0-23 hours)
- Days increment when hour goes past 23

### Career Progression

**Getting Promoted:**
- Work to gain experience points (+10 per shift)
- Each level requires experience threshold
- 3 levels total per career
- Auto-promoted when threshold is met
- Resets experience after promotion

---

## 🏗️ Architecture

### Package Structure

```
simsgame/
├── Main.java                  # Entry point
├── ConsoleUI.java             # Presentation layer (ANSI colors, menus, input)
│
├── engine/
│   ├── GameEngine.java        # Game state management
│   ├── GameContext.java       # Time and notification context
│   └── ActivityRegistry.java  # Activity definitions
│   └── Time.java              # Time manager
│
├── sims/
│   ├── SimCharacter.java      # Sim entity (composition root)
│   ├── Needs.java             # 5 needs with decay logic
│   ├── Mood.java              # Mood calculation
│   ├── Wallet.java            # Money management
│   ├── Family.java            # Family/children system
│   └── Child.java             # Child lifecycle
│
├── activity/
│   ├── Activity.java          # Abstract activity
│   ├── EatActivity.java
│   ├── SleepActivity.java
│   ├── ShowerActivity.java
│   ├── SocialiseActivity.java
│   ├── PlayActivity.java
│   └── WorkActivity.java
│
└── career/
    ├── Career.java            # Abstract career
    ├── CareerPromotion.java   # Promotion logic
    ├── CareerFactory.java     # Career creation
    ├── ShiftJob.java
    ├── FixedTimeJob.java
    └── CommissionJob.java
```

### Design Patterns Used

1. **MVC Pattern** - ConsoleUI (View) ↔ GameEngine (Controller) ↔ SimCharacter (Model)
2. **Composition** - SimCharacter has-a Needs, Mood, Wallet, Career
3. **Factory Pattern** - CareerFactory creates career instances
4. **Template Method** - Activity and Career abstract classes
5. **Strategy Pattern** - Different career types implement work() differently

### OOP Principles

- **Encapsulation** - Private fields with getters/setters
- **Inheritance** - Activity/Career abstract base classes
- **Polymorphism** - Different activities/careers via common interface
- **Abstraction** - GameEngine hides complexity from ConsoleUI

---

## 📊 Game Tips & Strategy

### Survival Tips

1. **Always keep Hunger and Energy above 0** - These can kill you!
2. **Watch for warnings** - Yellow/Red warnings appear automatically
3. **Balance work and needs** - Don't work when needs are low
4. **Sleep is free** - Use sleep to restore Energy without spending money
5. **Shower is free** - Use shower to maintain Hygiene

### Optimal Strategy

**Early Game (Days 1-3):**
1. Assign a career immediately (Option 7)
2. Work to build initial money reserve ($100+)
3. Keep all needs above 30
4. Focus on Hunger and Energy (survival needs)

**Mid Game (Days 4-10):**
1. Work regularly to maintain income
2. Keep mood HAPPY for slower need decay
3. Use free activities when possible (Sleep, Shower, Social)
4. Build toward promotions

**Late Game (Days 10+):**
1. Maintain high-level career
2. Balance all 5 needs for optimal mood
3. Accumulate wealth
4. Consider family expansion

---

## 🎯 Game Goals

This is a **sandbox survival game** - there's no "winning", but here are some goals:

- ✅ Survive for 30 days
- ✅ Reach maximum career level (Level 2)
- ✅ Accumulate $1000+
- ✅ Maintain HAPPY mood for 10 consecutive days
- ✅ Raise a child to adulthood
- ✅ Manage multiple Sims simultaneously
- ✅ Never let any need drop below 20

---

## 🐛 Known Issues

- Emoji rendering may vary by terminal/font
- Progress bars may not align perfectly in some terminals
- Color support required (ANSI codes)

---

## 📝 Credits

**Course:** CSC1109 - Object-Oriented Programming <br>
**Institution:** Singapore Institute of Technology (SIT) <br>
**Trimester:** Year 1, Tri 2 <br>
**Group 13:** Macarius, Wesley, Yu Xuan, Ann Ya, Kent, Lennon, Jennie <br>
**Inspired by:** The Sims by Maxis/EA

---

## 📜 License

This is an educational project for coursework purposes.

---

**Enjoy the game and keep your Sim alive! 🎮✨**
