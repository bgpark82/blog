#!/bin/bash

echo "start commit"
cd /Users/bgpark/Desktop/alethio/test/blog/hexo-beantech/source/_posts
echo date
echo git status
git add .
git commit -m "update"
git push
echo date
echo "end commit"

