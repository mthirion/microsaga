docker build -t mthirion/wiremock .
docker run -v volume:/data --net=host mthirion/wiremock
#docker run -v volume:/data -p 9999:9999 mthirion/wiremock
