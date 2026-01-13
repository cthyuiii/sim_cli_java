# Changelog - Text-Based Sims Game

## Version 2.0 - Enhanced Edition

### New Features

#### 🎭 Mood System
- **Dynamic Mood Calculation**: Mood is now calculated based on the average of all five needs
- **Six Mood States**: Ecstatic, Happy, Content, Uncomfortable, Miserable, and Icky
- **Mood Emojis**: Visual representation of current mood state (😄, 😊, 🙂, 😟, 😢, 🤢)
- **Mood Effects on Gameplay**: 
  - Good moods (Ecstatic/Happy) slow down need degradation
  - Bad moods (Uncomfortable/Miserable) speed up need degradation
  - Icky mood (triggered by hygiene < 20%) causes extreme energy drain
- **Mood Multipliers**: Range from 0.7x (Ecstatic) to 1.8x (Icky) degradation rate

#### 💼 Career System
- **Five Career Paths**: 
  - Fast Food Worker (quick shifts, lower pay)
  - Office Worker (balanced career)
  - Programmer (high pay, good hours)
  - Artist (creative, flexible)
  - Doctor (highest pay, longest hours)
- **Three-Level Progression**: Each career has entry, mid, and senior levels
- **Automatic Promotions**: Get promoted after completing required number of shifts
- **Increasing Pay Rates**: Higher levels earn significantly more per shift
- **Career-Specific Work Hours**: Different careers require different shift lengths (4-8 hours)
- **Dynamic Energy Requirements**: Longer shifts require more energy to start
- **Career Change Option**: Ability to switch careers (resets progression)
- **Experience Tracking**: Track total shifts worked and progress to next promotion

#### 🎮 Enhanced Gameplay
- **Career Info Display**: Detailed career statistics shown in status screen
- **Promotion Notifications**: Celebration message when achieving a promotion
- **Energy-Based Work Requirements**: Can't work if energy is too low for the shift length
- **Enhanced Status Display**: Mood now prominently displayed with needs
- **Special Icky Warning**: Urgent warning when in Icky mood state
- **Career Stats in Game Over**: Final career level and title shown at game end

### Technical Improvements
- **New Career Class**: Separate class for managing career logic and progression
- **Refactored SimCharacter**: Enhanced with mood calculation and career integration
- **Updated Game Loop**: Added career selection and change career options
- **Improved Work Method**: Dynamic energy requirements based on career
- **Mood Multiplier System**: Affects all time-based need degradation

### Balance Changes
- Work energy requirement increased from flat 30 to career-dependent (30-50)
- Mood system adds strategic depth to need management
- Career progression provides long-term goals
- Different careers offer varied gameplay experiences

## Version 1.0 - Original Release

### Features
- Character creation and naming
- Five core needs: Hunger, Energy, Hygiene, Social, Fun
- Time-based gameplay with day/night cycle
- Money system
- Eight activities: Eat, Sleep, Shower, Socialize, Play Games, Watch TV, Work, Exercise
- Visual status bars
- Warning system for low needs
- Game over conditions
