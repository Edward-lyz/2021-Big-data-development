/*
按月查看性别、商品类别的销售额
*/
SELECT DATE_FORMAT(`sale-date`,'%Y%m') months,`category` as category , `sex` as sex, sum(`sale-number`) as sales
FROM `商品` 
INNER JOIN `用户` 
ON `商品`.`user-id`=`用户`.`user-id`
GROUP BY sex, months, category