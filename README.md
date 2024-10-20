# Meeting-Calendar-Assistant
 Main API's
   You can book meeting using /meeting/book/{employeeId}
   To check freeslot for two employee -/meeting/freeSlots/{employeeId1}/{employeeId2}/{durationTime} which will give the freeslots for the specific day
   To check the conflicts separately /meeting/conflits  will return the employee that have conflicts with the meeting timing

Created using SpringMVC (Model-View-Controller)
And postgresSql is used.
