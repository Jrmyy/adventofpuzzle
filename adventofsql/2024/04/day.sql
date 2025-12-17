with
  previous_tags as (
    select
      toy_id
      , unnest(previous_tags) as tag
    from toy_production
  )

  , new_tags as (
    select
      toy_id
      , unnest(new_tags) as tag
    from toy_production
  )

  , unchanged as (
    select
      toy_id
      , tag
    from previous_tags

    intersect

    select
      toy_id
      , tag
    from new_tags
  )

  , added as (
    select
      toy_id
      , tag
    from new_tags

    except

    select
      toy_id
      , tag
    from previous_tags
  )

  , removed as (
    select
      toy_id
      , tag
    from previous_tags

    except

    select
      toy_id
      , tag
    from new_tags
  )

  , unchanged_aggregated as (
    select
      toy_id
      , array_agg(tag) as tags
    from unchanged
    group by toy_id
  )

  , added_aggregated as (
    select
      toy_id
      , array_agg(tag) as tags
    from added
    group by toy_id
  )

  , removed_aggregated as (
    select
      toy_id
      , array_agg(tag) as tags
    from removed
    group by toy_id
  )

select
  added_aggregated.toy_id
  , coalesce(cardinality(added_aggregated.tags), 0) as added
  , coalesce(cardinality(unchanged_aggregated.tags), 0) as unchanged
  , coalesce(cardinality(removed_aggregated.tags), 0) as removed
from added_aggregated
left join removed_aggregated
  using (toy_id)
left join unchanged_aggregated
  using (toy_id)
order by 2 desc
