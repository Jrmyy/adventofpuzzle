with
  plays as (
    select
      user_plays.song_id
      , songs.song_title
      , count(user_plays.user_id) as total_plays
      , count(user_plays.user_id) filter (where songs.song_duration != user_plays.duration) as total_skips
    from user_plays
    inner join songs using (song_id)
    group by 1, 2
  )
select
  song_title
from plays
order by total_plays desc, total_skips
