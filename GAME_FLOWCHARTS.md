# Sims Game - Flow Charts

This document contains flowcharts describing the game flow of the text-based Sims game.

---

## 1. Main Game Loop

```mermaid
flowchart TD
    A([Start Game<br/>Initialize GameEngine]) --> B[Start New Game<br/>Clear sims, reset time]
    B --> C[Print Dashboard<br/>- Status Card<br/>- Quick Actions Menu<br/>- Warnings]
    C --> D{First Run?<br/>No Sims?}
    D -->|Yes| E[Options<br/>9 or 0]
    D -->|No| F[All Menu Options<br/>0-9]
    E --> G[Process User Choice<br/>1-6: Quick Actions Activity<br/>7: Career Management<br/>8: Family Options<br/>9: Sim Management<br/>0: Quit Game]
    F --> G
    G --> H[Activity Flow]
    G --> I[Career Flow]
    G --> J[Family Flow]
    H --> K{Choice = 0<br/>Quit?}
    I --> K
    J --> K
    K -->|No| C
    K -->|Yes| L([End Game])
```

---

## 2. Activity Execution Flow

```mermaid
flowchart TD
    A[User Selects Activity 1-6] --> B{Has Active<br/>Sim?}
    B -->|No| C[Error Return]
    B -->|Yes| D{Sim Alive?}
    D -->|No| E[Error<br/>Deceased]
    D -->|Yes| F{Sim Busy?}
    F -->|Yes| G[Error<br/>Busy X hours]
    F -->|No| H{Can Afford?<br/>Check cost}
    H -->|No| I[Error<br/>Cost]
    H -->|Yes| J{Special Check<br/>Work = Career?}
    J -->|Fail| K[Error<br/>No Career]
    J -->|Pass| L[Perform Activity<br/>- Apply effects<br/>- Mark sim busy]
    L --> M{All Living Sims<br/>Busy?}
    M -->|Yes| N[Advance Time<br/>see Flow #3]
    M -->|No| O[Rotate to Next<br/>Available Sim]
    N --> O
    O --> P[Show Action Result<br/>- Time advanced<br/>- Needs decay]
    P --> Q[Handle Pending<br/>Births if any]
```

---

## 3. Time Advancement & Updates Flow

```mermaid
flowchart TD
    A[advanceTime hours<br/>Called when all sims busy] --> B[Calculate minimum busy time<br/>among all living sims]
    B --> C[Advance Game Time<br/>Update day/hour<br/>days_passed = time.advance hours]
    C --> D[For Each Living Sim]
    D --> E[Per-Hour Updates<br/>1. Needs decay by hours<br/>2. Mood update based on needs<br/>3. Check death conditions<br/>4. Reduce busy time]
    E --> F{Any Days<br/>Passed?}
    F -->|No| G[Time Update Complete]
    F -->|Yes| H[For Each Day]
    H --> I[For Each Sim<br/>Process Daily Events]
    I --> J[Family.onNewDay day<br/>see Family Flow #4]
    J --> K[Process Newborns<br/>- Add to pending list<br/>- Notify player]
    K --> L[Process Neglected Children<br/>- Remove from family<br/>- Notify death]
    L --> M[Process Grown-Up Children<br/>- Create new SimCharacter<br/>- Add to sim list<br/>- Remove from family<br/>- Notify growth]
    M --> G
```

---

## 4. Family & Child Events Flow

```mermaid
flowchart TD
    A[Family.onNewDay currentDay<br/>Called during time advancement] --> B{Is Sim<br/>Pregnant?}
    B -->|No| C[For Each Child]
    B -->|Yes| D{Days Since Pregnancy<br/>>= pregnancyDuration?<br/>3 days}
    D -->|No| C
    D -->|Yes| E[Generate Baby<br/>- Random gender<br/>- Create Child<br/>- Clear pregnancy<br/>- Add to newborns]
    E --> C
    C --> F[child.updateAge day<br/>- Increment age<br/>- Update care needs]
    F --> G{Age >= 7?<br/>No more care}
    G -->|Yes| H[needCare = false]
    G -->|No| I{Check Neglect<br/>needCare &&<br/>daysWithoutCare>=7?}
    H --> I
    I -->|Yes| J[Add to Neglected List]
    I -->|No| K{Check Growth<br/>Age >= 14?}
    J --> K
    K -->|Yes| L[Add to Grown-Up List]
    K -->|No| M[Return Lists:<br/>- Newborns<br/>- Neglected<br/>- Grown-Ups]
    L --> M
```

---

## 5. Sim Creation/Selection Flow

```mermaid
flowchart TD
    A[User Selects Option 9<br/>Sim Management] --> B{Any Sims<br/>Exist?}
    B -->|No| C[Create Sim Flow<br/>Auto]
    B -->|Yes| D[Display Sim List<br/>- Show active sim ★<br/>- Show deceased sims 💀<br/>- Show create option]
    C --> D
    D --> E{User Choice}
    E -->|Select Existing| F[Set Active Sim Index]
    E -->|Create New| G[Enter Name<br/>Letters only]
    E -->|Back 0| H[Return to Dashboard]
    G --> I[Enter Gender<br/>Male/Female]
    I --> J[Create SimCharacter<br/>- Age: 18<br/>- Money: $100<br/>- Default needs]
    J --> K[Add to Sim List]
    K --> L{First Sim?}
    L -->|Yes| M[Auto-set Active]
    L -->|No| F
    M --> F
    F --> N[Success Message]
```

---

## 6. Death Check Flow

```mermaid
flowchart TD
    A[checkDeath context<br/>Called after needs decay] --> B{Hunger<br/><= 0?}
    B -->|No| C[Check Hunger > 0<br/>Reset hungerZeroDay if needed]
    B -->|Yes| D{First Day at 0?<br/>hungerZeroDay == -1}
    D -->|Yes| E[Record Day<br/>hungerZeroDay = today]
    D -->|No| F{Days at 0<br/>>= 1?}
    E --> F
    F -->|Yes| G[Mark Dead<br/>alive = false<br/>Notify: starvation]
    F -->|No| H{Energy<br/><= 0?}
    C --> H
    G --> H
    H -->|No| I[Check Energy > 0<br/>Reset energyZeroDay if needed]
    H -->|Yes| J{First Day at 0?<br/>energyZeroDay == -1}
    J -->|Yes| K[Record Day<br/>energyZeroDay = today]
    J -->|No| L{Days at 0<br/>>= 1?}
    K --> L
    L -->|Yes| M[Mark Dead<br/>alive = false<br/>Notify: exhaustion]
    L -->|No| N([End])
    I --> N
    M --> N
```

---

## Summary of Key Game Mechanics

### Busy System

- When a sim performs an activity, they become "busy" for the activity duration
- Busy sims cannot perform new actions
- When **all living sims** are busy, time automatically advances
- Time advances by the **minimum remaining busy time** among all sims
- After time advances, all sims' busy counters are reduced

### Time Advancement Triggers

1. **Automatic**: When all living sims become busy
2. **During advancement**:
   - Needs decay based on hours passed
   - Mood updates based on current needs
   - Death checks are performed
   - Daily events are processed (pregnancy, child aging)

### Family System

- **Pregnancy**: Takes 3 days, results in newborn
- **Newborns**: Require care, player must name them
- **Children**:
  - Age 0-6 days: Require daily care
  - Age 7+ days: No longer need care
  - Age 14+ days: Grow up into new playable Sim
- **Neglect**: Child dies if needing care for 7+ days without care

### Death System

- Occurs when **Hunger** or **Energy** remains at 0 for 1 full day
- Game continues until all sims are dead
- Dead sims cannot perform actions but remain in the sim list

### Activity Flow Summary

1. User selects sim (or game auto-rotates to available sim)
2. User selects activity
3. Activity checks pass (alive, not busy, afford, requirements)
4. Activity is applied (needs change, money changes)
5. Sim becomes busy for activity duration
6. **If all living sims busy**: Time advances → Needs decay → Mood update → Family events → Death checks
7. Game rotates to next available sim
8. User sees results and can continue

This architecture ensures a turn-based simulation where time progresses naturally based on sim actions and busy states.
