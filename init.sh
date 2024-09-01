docker network create -d bridge dev_bridge  # only once
mkdir DB/pgData  #only once

#Run everytime
docker compose build
docker compose up