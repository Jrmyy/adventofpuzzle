year=$1
day=$2
echo "Running for Y:$year D:$day"

path=$year/input_$day.sql

echo "Cleaning docker setup..."
docker compose down -v
docker compose up -d --wait

echo "Executing data load..."
docker compose exec -T postgres psql -U dbuser aos < $path
