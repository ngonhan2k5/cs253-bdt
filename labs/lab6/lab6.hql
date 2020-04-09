-- set hive.cli.print.header=true;
DROP TABLE employee;
DROP VIEW employee_vw;
--CSV header: Name,Job Titles,Department,Full or Part-Time,Salary or Hourly,Typical Hours,Annual Salary,Hourly Rate
CREATE EXTERNAL table employee (
        name STRING, 
        job_title STRING, 
        department STRING, 
        job_type STRING, 
        sal_type STRING, 
        typical_hours INT, 
        annual_sal FLOAT, 
        hour_rate FLOAT)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
tblproperties ("skip.header.line.count"="1");

LOAD DATA LOCAL INPATH 'input/Current_Employee_Names__Salaries__and_Position_Titles.csv'
OVERWRITE INTO TABLE employee;

-- ALTER TABLE employee CHANGE annual_sal annual_sal FLOAT;

-- create view to cast type
CREATE VIEW employee_vw AS
SELECT 
        name, 
        job_title, 
        department, 
        job_type, 
        sal_type, 
        CAST(typical_hours AS INT) AS typical_hours, 
        CAST(annual_sal AS FLOAT) AS annual_sal, 
        CAST(hour_rate AS FLOAT) AS hour_rate
FROM employee;

-- 3 queries
-- 1. Find top ten hourly salary job title 
SELECT job_title, MAX(hour_rate) AS max_hour_rate  
FROM employee_vw  
WHERE sal_type = 'Hourly'
GROUP BY job_title 
ORDER BY max_hour_rate DESC LIMIT 10;

-- 2. Find top 10 salary employee, if have 52 weeks per year
SELECT name, sal_type, salary,hour_rate, typical_hours FROM 
    (
        SELECT 
            name, CASE sal_type WHEN 'Hourly' THEN hour_rate*typical_hours*52 WHEN 'Salary' THEN annual_sal ELSE 0 END AS salary, 
            sal_type, hour_rate,typical_hours
        FROM employee_vw
    ) SalaryTbl
 ORDER BY salary DESC
 LIMIT 10;

 -- 3. Find top ten expensed departments 
SELECT department, CAST(SUM(salary) AS DECIMAL)  AS sum_salary, COUNT(*) AS emp_count FROM
    (   
        SELECT
            name, CASE sal_type WHEN 'Hourly' THEN hour_rate*typical_hours*52 WHEN 'Salary' THEN annual_sal ELSE 0 END AS salary, 
            department,
            sal_type, hour_rate,typical_hours
        FROM employee_vw
    ) SalaryTbl
GROUP BY department
ORDER BY sum_salary DESC
LIMIT 10;

-- select count(*) from employee_vw where department = 'POLICE';