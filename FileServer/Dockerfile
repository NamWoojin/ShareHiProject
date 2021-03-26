FROM node
WORKDIR /usr/src/app
COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 9000 9001

CMD ["node", "./app.js"]

