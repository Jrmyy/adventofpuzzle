with
  sleigh_bell_wisp_body_parts as (
    select
      body_part
    from creature_body_parts
    where
      creature = 'Sleigh Bell Wisp'
  )

  , sleigh_bell_wisp_body_part_affinities as (
    select
      body_part_affinities.property
      , body_part_affinities.body_part
      , body_part_affinities.multiplier
    from body_part_affinities
    inner join sleigh_bell_wisp_body_parts
      using (body_part)
    where
      creature = 'Sleigh Bell Wisp'
  )

  , hearthfire_torch_properties as (
    select
      property
    from weapon_properties
    where
      weapon = 'Hearthfire Torch'
  )

  , sleigh_bell_wisp_body_part_damages as (
    select
      sleigh_bell_wisp_body_part_affinities.body_part
      , sleigh_bell_wisp_body_part_affinities.multiplier * property_effects.base_damage as total_damage
    from sleigh_bell_wisp_body_part_affinities
    inner join hearthfire_torch_properties
      using (property)
    inner join property_effects
      using (property)
  )

select
  body_part
  , sum(total_damage)
from sleigh_bell_wisp_body_part_damages
group by body_part
order by 2 desc
limit 1
