FROM node
WORKDIR /usr/src/app
COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 9002 9003

CMD ["node", "./appV2.js"]

