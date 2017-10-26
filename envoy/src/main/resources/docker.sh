docker build -t mthirion/envoy .
docker run -d -p 9998:9998 --net=host mthirion/envoy
#docker exec -it 3d735fa7207e /bin/bash
