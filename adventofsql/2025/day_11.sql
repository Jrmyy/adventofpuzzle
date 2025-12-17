-- Write your SQL query id id
with
  creature_properties as (
    select
      property
      , type
    from creature_weaknesses
    where
      creature = 'Sleigh Bell Wisp'
  )

  , forbidden_properties as (
    select
      property
    from creature_properties
    where
      type = 'forbidden'
  )

  , weakness_properties as (
    select
      property
    from creature_properties
    where
      type = 'weakness'
  )

  , weapon_matching_weaknesses as (
    select
      weapon_properties.weapon
      , count(weapon_properties.property) as strong_against
    from weapon_properties
    inner join weakness_properties
      using (property)
    group by 1
  )

  , weapon_matching_forbidden as (
    select
      weapon_properties.weapon
      , count(weapon_properties.property) as weak_against
    from weapon_properties
    inner join forbidden_properties
      using (property)
    group by 1
  )

select
  weapon_matching_weaknesses.weapon
from weapon_matching_weaknesses
left join weapon_matching_forbidden
  using (weapon)
where
  weapon_matching_weaknesses.strong_against = (
    select
      count(*)
    from weakness_properties
  )
  and weapon_matching_forbidden.weak_against is null
