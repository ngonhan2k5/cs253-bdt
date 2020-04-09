/* (160565,The Purge: Election Year (2016),Action|Horror|Sci-Fi) */
Users = LOAD 'input_part2/users.txt' USING PigStorage('|')
                AS (userId, age, gender, occupation, zipCode);

MaleLawyers = FILTER Users BY gender == 'M' AND occupation == 'lawyer';
MaleLawyerGroup = GROUP MaleLawyers ALL;
MaleLawyerCount = FOREACH MaleLawyerGroup GENERATE 'Number-of-Male-Lawyer', COUNT(MaleLawyers);


rmf -rm -r output_1
STORE MaleLawyerCount INTO 'output_1';
DUMP MaleLawyerCount;
