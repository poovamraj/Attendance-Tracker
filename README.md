# Attendance-Tracker
An Attendance tracker that employee can use to put attendance, check permissions left, check leaves left and other statistics.
The code for server is provided in a different branch in the same repo.
The server keeps track of time of entering and exit. It can reduce permission count and leave count automatically if the employee is late or is absent.
If the leave count or permission count hits zero the app a prompt is provided to meet the employer.
User can keep track of their statistics but only theirs. Administrator with the password can run a query to search for any employee statistics.
SQL Injection cant be performed.
An interactive graph is provided for seeing the statistics along with the Attendance percentage and permission and leave count remaining
