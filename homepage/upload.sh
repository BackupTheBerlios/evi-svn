iconv --from=utf-8 --to=iso-8859-1 index.copl.de | ./copli --nohttp >index.html.de \
&& iconv --from=utf-8 --to=iso-8859-1 download.copl.de | ./copli --nohttp >download.html.de \
&& iconv --from=utf-8 --to=iso-8859-1 development.copl.de | ./copli --nohttp >development.html.de \
&& ./copli --nohttp index.copl.en index.html.en \
&& ./copli --nohttp download.copl.en download.html.en \
&& ./copli --nohttp development.copl.en development.html.en \
&& tar jcvf homepage.tar.bz2 --exclude=*.svn* --exclude=copli --exclude=homepage.tar.bz2 --exclude=*upload.sh* . \
&& scp -r homepage.tar.bz2 schwering@shell.berlios.de:/home/groups/evi/htdocs
rm homepage.tar.bz2 \
&& ssh -l schwering shell.berlios.de "cd /home/groups/evi/htdocs && (tar jxvf homepage.tar.bz2; rm homepage.tar.bz2; cd sitemap_gen-1.4; python sitemap_gen.py --config=config.xml)"
