REGISTER /usr/lib/pig/piggybank.jar

/* (160565,The Purge: Election Year (2016),Action|Horror|Sci-Fi) */
Movies = LOAD 'input_part2/movies.csv' USING org.apache.pig.piggybank.storage.CSVExcelStorage() 
                AS (movieID:int, title:chararray, genres:chararray);
                
AdvMovies = FILTER Movies BY INDEXOF(genres,'Adventure') > -1;

Ratings = LOAD 'input_part2/rating.txt' AS (userId, movieId:int, rating:int, timestamp);
HighestRatings = FILTER Ratings BY rating == 5;
HighestRatingsDist = DISTINCT (FOREACH HighestRatings GENERATE movieId, rating);

Joined = JOIN AdvMovies BY movieID, HighestRatingsDist BY movieId;

Top20 = LIMIT (ORDER Joined BY movieID) 20;
Top20 = FOREACH Top20 GENERATE movieID, 'Adventure', rating, title;


rmf -rm -r output_4
STORE Top20 INTO 'output_4';
DUMP Top20;