year=$1
day=$2
echo "Running for Y:$year D:$day"

path=$year/$day/input.sql

echo "Cleaning docker setup..."
docker compose down -v
docker compose up -d --wait

echo "Executing data load..."
docker compose exec -T postgres psql -U dbuser aos < $path
