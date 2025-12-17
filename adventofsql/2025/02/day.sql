select
  elves.name
  , count(distinct elf_checkins.work_date)
from elves
inner join elf_checkins on elves.id = elf_checkins.elf_id
inner join checkins on checkins.checkin_date = elf_checkins.work_date
inner join reindeer on checkins.reindeer_id = reindeer.id
where
  reindeer.name = 'Blitzen'
group by 1
order by 2 desc
limit 1
