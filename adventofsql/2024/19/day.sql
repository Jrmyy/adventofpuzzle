with
    employees_with_last_performance as (
        select *
            , year_end_performance_scores[array_upper(year_end_performance_scores, 1)] as last_performance_score
        from
            employees
    )

    , average_last_performance as (
        select
            avg(last_performance_score) as global_average_performance_score
        from
            employees_with_last_performance
    )
    , actual_salaries as (
        select
            case
                when employees_with_last_performance.last_performance_score >
                     average_last_performance.global_average_performance_score
                    then 1.15 * employees_with_last_performance.salary
                else salary
                end as salary
        from
            employees_with_last_performance
            inner join average_last_performance on true
    )

select
    round(sum(salary), 2) as total_salary_with_bonuses
from
    actual_salaries
