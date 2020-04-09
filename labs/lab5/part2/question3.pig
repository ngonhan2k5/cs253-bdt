REGISTER /usr/lib/pig/piggybank.jar;

/* (160565,The Purge: Election Year (2016),Action|Horror|Sci-Fi) */
Movies = LOAD 'input_part2/movies.csv' USING org.apache.pig.piggybank.storage.CSVExcelStorage() 
                AS (movieID:int, title:chararray, genres:chararray);

AMovies = FILTER Movies BY STARTSWITH(UCFIRST(title), 'A');
AMoviesByGenres = FOREACH AMovies GENERATE TOKENIZE(genres, '|') as genres;
AMoviesByGenresFlat = FOREACH AMoviesByGenres GENERATE FLATTEN(genres) AS genre;

AMoviesGroupGenres = GROUP AMoviesByGenresFlat BY genre;

AMoviesGroupGenresCounts = FOREACH AMoviesGroupGenres GENERATE group, COUNT(AMoviesByGenresFlat);
AMoviesGroupGenresCountsSorted = ORDER AMoviesGroupGenresCounts BY group;

rmf -rm -r output_3
STORE AMoviesGroupGenresCountsSorted INTO 'output_3';
DUMP AMoviesGroupGenresCountsSorted;
