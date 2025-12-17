with
  number_of_traits as (
    select
      creature
      , count(distinct fragment) as required_traits
    from creature_traits
    group by 1
  )

  , matching_traits as (
    select
      creature_traits.creature
      , count(distinct creature_traits.fragment) as matched_traits
    from creature_traits
    inner join rune_fragments
      on creature_traits.fragment = rune_fragments.fragment
    where
      rune_fragments.sig_hash = 'VOID-7F3C'
    group by 1
    order by 2 desc
  )

select
  matching_traits.creature
from matching_traits
inner join number_of_traits
  using (creature)
where
  number_of_traits.required_traits = matching_traits.matched_traits
