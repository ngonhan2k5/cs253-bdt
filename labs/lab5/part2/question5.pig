REGISTER /usr/lib/pig/piggybank.jar

/* (160565,The Purge: Election Year (2016),Action|Horror|Sci-Fi) */
Movies = LOAD 'input_part2/movies.csv' USING org.apache.pig.piggybank.storage.CSVExcelStorage() 
                AS (movieID:int, title:chararray, genres:chararray);
                
AdvMovies = FILTER Movies BY INDEXOF(genres,'Adventure') > -1;

Ratings = LOAD 'input_part2/rating.txt' AS (userId:int, movieId:int, rating:int, timestamp);
HighestRatings = FILTER Ratings BY rating == 5;
HighestRatingsDist = DISTINCT (FOREACH HighestRatings GENERATE movieId, rating);

-- HighestRatingsGrouped = GROUP HighestRatings BY movieId;

Joined = JOIN AdvMovies BY movieID, HighestRatingsDist BY movieId;

Top20 = LIMIT (ORDER Joined BY movieID) 20;
Top20 = FOREACH Top20 GENERATE movieID, 'Adventure', rating, title;

Users = LOAD 'input_part2/users.txt' USING PigStorage('|') AS (userId:int, age, gender, occupation, zipCode);
MaleProgramers = FILTER Users BY gender == 'M' and occupation == 'programmer';
RatingByMaleProgramers = JOIN Ratings BY userId, MaleProgramers by userId;

Top20Rating = JOIN Top20 BY movieID, RatingByMaleProgramers BY movieId;
Top20RatingGrouped =  GROUP Top20Rating ALL;
Top20RatingGroupedCount = FOREACH Top20RatingGrouped GENERATE COUNT(Top20Rating);

rmf -rm -r output_5
STORE Top20RatingGroupedCount INTO 'output_5';
DUMP Top20RatingGroupedCount;
