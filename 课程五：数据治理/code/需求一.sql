/*
按月份查看新增用户
*/
select DATE_FORMAT(date,'%Y%m') months,count('user-id') as 新增用户 from `用户` group by months;

/*
按月份查询销售总额
*/
SELECT DATE_FORMAT(`sale-date`,'%Y%m') months,sum(`sale-number`) as 销售额 from `商品` group by months;
