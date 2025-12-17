select
  teleport_log.sig_hash
from teleport_log
left join known_beings
  using (sig_hash)
where
  known_beings.sig_hash is null
order by teleport_log.energy desc
limit 1
