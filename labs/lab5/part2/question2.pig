/* (160565,The Purge: Election Year (2016),Action|Horror|Sci-Fi) */
Users = LOAD 'input_part2/users.txt' USING PigStorage('|')
                AS (userId, age, gender, occupation, zipCode);

MaleLawyers = FILTER Users BY gender == 'M' AND occupation == 'lawyer';
MaleLawyerOldest = LIMIT (ORDER MaleLawyers BY age) 1;
MaleLawyerOldestID = FOREACH MaleLawyerOldest GENERATE 'Oldest-Male-Lawyer-ID', userId;

rmf -rm -r output_2
STORE MaleLawyerOldestID INTO 'output_2';
DUMP MaleLawyerOldestID;
