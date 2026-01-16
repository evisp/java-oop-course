# Sprint 3: JavaFX GUI and Full Use Case   

Complete the project by delivering a runnable, end-to-end application with a JavaFX GUI (at least two screens) and a clean separation between UI, business logic, and data/persistence layers.  

![Sprint 3](https://i.imgur.com/l7V7BAa.png)


## Goal   
Transform the Sprint 1-2 foundation into a complete, user-navigable application that demonstrates the full core workflow through a GUI (e.g., login → dashboard → key actions).     
Emphasize maintainability by separating concerns (UI vs logic vs data) while preserving the OOP design, exception handling, and persistence approach introduced earlier.   

## Key elements  
| Element | Details |
| --- | --- |
| Topics covered | JavaFX GUI (2+ screens), separation of concerns (UI/logic/data), meaningful collections (`List`, `HashMap`), complete use case integration.   |
| Teams | Same teams as Sprint 1–2 (2–4 students per team).    |
| Points | Sprint 3: 10 points (as continuation of the 3-sprint structure).    |
| Deadline | January 20 (end of day).   |
| Submission | One team submission: repository link (or ZIP) containing report + updated UML + source code.   |
| Report length | 7–10 pages.   |

## What to deliver  
- **Report (7–10 pages)** describing: the final project goal, the complete use case(s), the GUI flow between screens, and the separation of responsibilities between layers (UI, logic, data).    
- **Updated UML class diagram (PNG/PDF)** showing final classes and relationships, including abstractions from Sprint 2 and clear boundaries between layers (e.g., UI controllers → services → repositories).     
- **Source code** implementing all required features below in a clean, consistent, and runnable application.  

### Required features (code)  
- **Complete application**, meaning a user can complete the main workflow end-to-end (core actions + results), and data is correctly saved and restored across restarts.
- **JavaFX GUI with at least two screens**, such as a Login screen and a Dashboard screen, with working navigation between them.    
- **Separation of concerns**, with distinct packages/classes for:
  - UI layer (JavaFX views/controllers).    
  - Logic layer (services that implement rules/use cases).    
  - Data layer (repositories + file persistence used in Sprint 2).     
- **Meaningful collections usage**, demonstrating appropriate data structures such as:
  - `List` for ordered collections (e.g., enrollments, loans, attendance records).    
  - `HashMap` for fast lookup (e.g., users by username/id, items by code, sessions by date).    
- **Complete use case**, meaning the project’s main workflow can be executed fully by a user through the GUI (not only via console methods).  

## Grading rubric (Sprint 3)  
| Category | Points | Criteria |
| --- | --- | --- |
| Project defense | 3 | Team demonstrates the full workflow and explains key architectural/design decisions clearly.   |
| GUI & navigation | 2 | JavaFX includes 2+ screens with correct navigation and a coherent user journey (e.g., login → dashboard actions).   |
| Architecture quality | 3 | Clear separation of UI/logic/data with sensible responsibilities and low coupling.   |
| Collections & completeness | 2 | Appropriate `List`/`HashMap` usage and a complete, working project use case.   |
| **Total** | **10** |    |

## Submission checklist  
- Same team as Sprint 1–2, continuing from the existing codebase.     
- JavaFX application runs and includes at least two screens with working navigation.    
- Report is 7–10 pages and clearly documents: GUI flow, layered design, and collection usage.    
- ZIP/repository includes report, updated UML diagram, and fully functional source code, submitted by January 21.  
