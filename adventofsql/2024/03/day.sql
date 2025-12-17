with
  prepared_data as (
    select
      (
        (
          xpath(
            '/polar_celebration/event_administration/participant_metrics/attendance_details/headcount/total_present/text()',
            menu_data
          )
          )[1]
        )::varchar::int as head_count
      , (
        xpath(
          '/polar_celebration/event_administration/culinary_records/menu_analysis/item_performance/food_item_id/text()',
          menu_data)
        ) as food_entries
    from christmas_menus
    where
      xpath_exists('/polar_celebration', menu_data)

    union all

    select
      (
        (
          xpath(
            '/christmas_feast/organizational_details/attendance_record/total_guests/text()',
            menu_data
          )
          )[1]
        )::varchar::int as head_count
      , (
        xpath(
          '/christmas_feast/organizational_details/menu_registry/course_details/dish_entry/food_item_id/text()',
          menu_data)
        ) as food_entries
    from christmas_menus
    where
      xpath_exists('/christmas_feast', menu_data)

    union all

    select
      (
        (
          xpath(
            '/northpole_database/annual_celebration/event_metadata/dinner_details/guest_registry/total_count/text()',
            menu_data
          )
          )[1]
        )::varchar::int as head_count
      , (
        xpath(
          '/northpole_database/annual_celebration/event_metadata/menu_items/food_category/food_category/dish/food_item_id/text()',
          menu_data)
        ) as food_entries
    from christmas_menus
    where
      xpath_exists('/northpole_database', menu_data)
  )

  , matching_food_items as (
    select
      unnest(food_entries::varchar[]::integer[]) as food_item_id
    from prepared_data
    where
      head_count > 78
  )

  , counted_food_items as (
    select
      food_item_id
      , count(*) as occurrences
    from matching_food_items
    group by 1
  )

select
  food_item_id
from counted_food_items
order by occurrences desc
limit 1
