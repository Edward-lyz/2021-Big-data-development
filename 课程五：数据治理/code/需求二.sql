/*
按月查看城市销售额
*/
SELECT `city` , DATE_FORMAT(`sale-date`,'%Y%m') months ,sum(`sale-number`) as sales
FROM `商品` 
INNER JOIN `用户` 
ON `商品`.`user-id`=`用户`.`user-id`
GROUP BY `city`,months