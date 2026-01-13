# Text-Based Sims Game - Enhanced Edition

A command-line simulation game where you manage a character's daily life by balancing various needs, managing moods, and progressing through a career.

## New Features

### 🎭 Mood System
Your Sim's mood is dynamically calculated based on all needs and affects gameplay:

| Mood | Emoji | Average Needs | Effect |
|------|-------|---------------|--------|
| **Ecstatic** | 😄 | 80-100% | Needs degrade 30% slower |
| **Happy** | 😊 | 60-79% | Needs degrade 10% slower |
| **Content** | 🙂 | 40-59% | Normal degradation |
| **Uncomfortable** | 😟 | 25-39% | Needs degrade 30% faster |
| **Miserable** | 😢 | 0-24% | Needs degrade 60% faster |
| **Icky** | 🤢 | Hygiene < 20% | Needs degrade 80% faster + Energy drains rapidly |

**Special Icky Mood Effect:** When hygiene drops below 20%, your Sim enters the "Icky" mood state. This causes energy to drain extremely fast (additional -5 per hour) and all needs degrade 80% faster. Take a shower immediately to recover!

### 💼 Career System
Choose from 5 different career paths, each with 3 levels of progression:

#### 1. Fast Food Worker
- **Level 1:** Crew Member - $40/shift (4 hours)
- **Level 2:** Shift Leader - $70/shift (4 hours) - *Requires 3 shifts*
- **Level 3:** Store Manager - $120/shift (4 hours) - *Requires 6 shifts*

#### 2. Office Worker
- **Level 1:** Intern - $60/shift (6 hours)
- **Level 2:** Junior Associate - $100/shift (6 hours) - *Requires 4 shifts*
- **Level 3:** Senior Manager - $180/shift (6 hours) - *Requires 8 shifts*

#### 3. Programmer
- **Level 1:** Junior Developer - $80/shift (5 hours)
- **Level 2:** Software Engineer - $140/shift (5 hours) - *Requires 5 shifts*
- **Level 3:** Lead Architect - $250/shift (5 hours) - *Requires 10 shifts*

#### 4. Artist
- **Level 1:** Freelance Artist - $50/shift (4 hours)
- **Level 2:** Gallery Artist - $110/shift (4 hours) - *Requires 4 shifts*
- **Level 3:** Master Artist - $200/shift (4 hours) - *Requires 9 shifts*

#### 5. Doctor
- **Level 1:** Medical Intern - $90/shift (8 hours)
- **Level 2:** Resident Doctor - $160/shift (8 hours) - *Requires 6 shifts*
- **Level 3:** Chief Surgeon - $300/shift (8 hours) - *Requires 12 shifts*

**Career Progression:** Work shifts to gain experience. When you reach the required number of shifts, you'll automatically get promoted with higher pay!

## Core Features

- **Character Management**: Create and name your Sim
- **Needs System**: Monitor and maintain 5 core needs:
  - Hunger (eat to survive)
  - Energy (sleep to recharge)
  - Hygiene (shower to stay clean)
  - Social (interact with others)
  - Fun (entertainment and activities)
- **Dynamic Mood System**: Your mood affects how fast needs degrade
- **Career Progression**: Choose a career, work shifts, and get promotions
- **Time-Based Gameplay**: Actions consume time and needs degrade over time
- **Money System**: Earn money by working and spend it on food
- **Multiple Activities**: 10 different actions to choose from
- **Game Over Conditions**: Survive as long as possible!

## How to Compile and Run

### Compilation
```bash
cd src
javac Game.java SimCharacter.java Career.java
```

### Running the Game
```bash
java Game
```

Or use the convenient run script:
```bash
./run.sh
```

## Gameplay Instructions

1. **Create Your Character**: Enter a name for your Sim when prompted
2. **Choose a Career**: Select from 5 different career paths
3. **Monitor Needs**: Keep an eye on the status bars - don't let them reach 0!
4. **Watch Your Mood**: Maintain good needs to keep a positive mood
5. **Work for Promotions**: Complete shifts to gain experience and get promoted
6. **Manage Resources**: Balance time and money to keep your Sim alive
7. **Survive**: Try to survive as many days as possible and reach max career level!

## Available Actions

| Action | Cost | Time | Effects |
|--------|------|------|---------|
| **Snack** | $5 | 1h | +15 Hunger |
| **Meal** | $15 | 2h | +40 Hunger, +5 Fun |
| **Feast** | $30 | 3h | +70 Hunger, +15 Fun, +10 Social |
| **Sleep** | Free | 1-12h | +15 Energy per hour |
| **Shower** | Free | 1h | +50 Hygiene (cures Icky mood!) |
| **Socialize** | Free | 2h | +30 Social, +15 Fun |
| **Play Games** | Free | 2h | +35 Fun |
| **Watch TV** | Free | 2h | +20 Fun |
| **Work** | Earn $ | Varies | Gain experience, earn money (energy requirement varies by career) |
| **Exercise** | Free | 2h | +10 Fun, -20 Energy, -15 Hunger (requires 15+ energy) |
| **Change Career** | Free | Instant | Switch careers (resets progression) |

## Tips for Survival

### General Tips
- **Monitor your mood** - A good mood makes survival easier
- **Work regularly** to earn money for food and get promotions
- **Sleep before energy drops too low** - you can't work when exhausted
- **Eat regularly** - starvation leads to game over
- **Balance your activities** - don't neglect any need for too long
- **Plan ahead** - some activities take several hours
- **Watch warnings** - alerts appear when needs drop below 30%

### Mood Management
- Keep all needs above 60% to maintain a Happy or Ecstatic mood
- Good moods slow down need degradation, making survival easier
- Bad moods accelerate need degradation - recover quickly!
- **AVOID THE ICKY MOOD** - Shower immediately if hygiene drops below 20%

### Career Strategy
- **Fast Food**: Best for quick money with short shifts
- **Office**: Balanced option with decent pay and moderate hours
- **Programmer**: High pay with good work-life balance (recommended!)
- **Artist**: Creative path with flexible hours
- **Doctor**: Highest pay but longest shifts (requires high energy)

### Energy Requirements for Work
- Fast Food/Artist: 30+ energy (4 hour shift)
- Programmer: 35+ energy (5 hour shift)
- Office: 40+ energy (6 hour shift)
- Doctor: 50+ energy (8 hour shift)

## Game Over Conditions

The game ends if:
- Hunger reaches 0 (starvation)
- Energy reaches 0 (exhaustion)
- You choose to quit

## Starting Resources

- Money: $500
- All needs: 80/100
- Start time: Day 1, 8:00 AM
- Mood: Ecstatic 😄
- Career: Your choice!

## Advanced Gameplay

### Reaching Max Level
Each career has unique progression requirements. The Programmer and Doctor careers take the longest to max out but offer the highest rewards. Can you reach Level 3 in your chosen career?

### Mood Optimization
Maintaining an Ecstatic mood (80%+ average needs) significantly extends your survival time. Focus on keeping all needs high rather than letting some drop critically low.

### Money Management
Balance earning money through work with spending on food. Higher-tier meals provide better value per hunger point but cost more upfront. Plan your meals around your work schedule.

## Game Statistics to Track

- Days survived
- Career level reached
- Total money earned
- Work shifts completed
- Highest mood maintained

Good luck managing your Sim's life!
