FROM node:18.20.5

WORKDIR /app

COPY package*.json ./

COPY prisma ./prisma/

RUN apt-get update && apt-get install -y \
    libc6 \
    libc6-dev

COPY . .

RUN npm install

EXPOSE 5000

CMD ["npm", "run", "start"]