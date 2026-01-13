# Text-Based Sims Game

A simple command-line simulation game where you manage a character's daily life by balancing various needs.

## Features

- **Character Management**: Create and name your Sim
- **Needs System**: Monitor and maintain 5 core needs:
  - Hunger (eat to survive)
  - Energy (sleep to recharge)
  - Hygiene (shower to stay clean)
  - Social (interact with others)
  - Fun (entertainment and activities)
- **Time-Based Gameplay**: Actions consume time and needs degrade over time
- **Money System**: Earn money by working and spend it on food
- **Multiple Activities**: 8 different actions to choose from
- **Game Over Conditions**: Survive as long as possible!

## How to Compile and Run

### Compilation
```bash
cd src
javac Game.java SimCharacter.java
```

### Running the Game
```bash
java Game
```

## Gameplay Instructions

1. **Create Your Character**: Enter a name for your Sim when prompted
2. **Monitor Needs**: Keep an eye on the status bars - don't let them reach 0!
3. **Choose Actions**: Select from the menu to perform activities
4. **Manage Resources**: Balance time and money to keep your Sim alive
5. **Survive**: Try to survive as many days as possible!

## Available Actions

| Action | Cost | Time | Effects |
|--------|------|------|---------|
| **Snack** | $5 | 1h | +15 Hunger |
| **Meal** | $15 | 2h | +40 Hunger, +5 Fun |
| **Feast** | $30 | 3h | +70 Hunger, +15 Fun, +10 Social |
| **Sleep** | Free | 1-12h | +15 Energy per hour |
| **Shower** | Free | 1h | +50 Hygiene |
| **Socialize** | Free | 2h | +30 Social, +15 Fun |
| **Play Games** | Free | 2h | +35 Fun |
| **Watch TV** | Free | 2h | +20 Fun |
| **Work** | Earn $50-100 | 4h | -25 Energy, -10 Fun, -15 Hunger (requires 30+ energy) |
| **Exercise** | Free | 2h | +10 Fun, -20 Energy, -15 Hunger |

## Tips for Survival

- **Work regularly** to earn money for food
- **Sleep before energy drops too low** - you can't work when exhausted
- **Eat regularly** - starvation leads to game over
- **Balance your activities** - don't neglect any need for too long
- **Plan ahead** - some activities take several hours
- **Watch warnings** - alerts appear when needs drop below 30%

## Game Over Conditions

The game ends if:
- Hunger reaches 0 (starvation)
- Energy reaches 0 (exhaustion)
- You choose to quit

## Starting Resources

- Money: $500
- All needs: 80/100
- Start time: Day 1, 8:00 AM

Good luck managing your Sim's life!
