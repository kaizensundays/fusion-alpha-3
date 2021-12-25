
docker build -t fusion-mu:latest .

docker tag fusion-mu:latest localhost:32000/fusion-mu:latest

docker push localhost:32000/fusion-mu:latest
