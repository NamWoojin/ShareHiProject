FROM node
WORKDIR /usr/src/app
COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 8002 8003

CMD ["node", "./appV2.js"]

