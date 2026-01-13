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


## Version 3.0 - Family & Procreation Edition

### New Features

#### 👶 Family and Procreation System
- **Try for Baby Action**: Spend $100 and time to attempt conception (70% success rate)
- **Pregnancy System**: 3-day pregnancy period with countdown display in status
- **Birth Event**: Automatic birth after pregnancy completes with celebration message
- **Child Growth Mechanics**: Children automatically age as game days pass
- **Five Life Stages**: Baby (0-2 days), Toddler (3-5 days), Child (6-10 days), Teen (11-15 days), Young Adult (16+ days)
- **Care for Children Action**: Spend time with babies and toddlers to boost social and fun
- **View Family Action**: Display all children with ages, stages, and growth progress
- **Growth Notifications**: Automatic messages when children reach new life stages
- **Family Size Limit**: Maximum of 5 children to keep gameplay manageable
- **Energy Impact**: Babies and toddlers drain energy faster over time
- **Legacy System**: Children count displayed in game over screen

#### 🎮 Enhanced Gameplay
- **Pregnancy Requirements**: Must have 50+ social, 40+ energy, and $100 to try for baby
- **Care Requirements**: Must have 20+ energy to care for children
- **Family Status Display**: Pregnancy countdown and children count shown in main status
- **Age-Dependent Care**: Only babies and toddlers require active care
- **Independence Milestones**: Children become independent at age 6 days
- **Growth Progress Indicators**: Shows days until next life stage for each child
- **Family Info in Status**: Quick summary of children and care needs in main display

### Technical Improvements
- **New Child Class**: Manages individual child data, age progression, and life stages
- **ArrayList Integration**: Dynamic family management with multiple children
- **Age Update System**: Automatic age progression when game day changes
- **Life Stage Transitions**: Automatic stage changes with emoji indicators
- **Pregnancy Tracking**: Day-based pregnancy system with automatic birth trigger
- **Energy Drain Calculation**: Dynamic energy cost based on number of young children

### Balance Changes
- Try for Baby costs $100 (significant investment)
- Pregnancy provides +20 social and +15 fun during conception attempt
- Birth drains -30 energy and -20 hunger but provides +30 social
- Caring for children costs -15 energy but provides +20 social and +10 fun
- Each baby/toddler drains additional -2 energy per hour passively
- Maximum 5 children to prevent overwhelming gameplay

### UI Improvements
- Pregnancy indicator (🤰) in status display
- Children count with care needs summary in status
- Age emoji indicators (👶🧒👦🧑👨) for each life stage
- Growth milestone notifications (🎂) when aging up
- Family status screen with detailed child information
- Next milestone countdown for each child
