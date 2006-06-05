compile() 
{
	iconv --from=utf-8 --to=iso-8859-1 index.copl.de | ./copli --nohttp >index.html.de \
	&& iconv --from=utf-8 --to=iso-8859-1 download.copl.de | ./copli --nohttp >download.html.de \
	&& iconv --from=utf-8 --to=iso-8859-1 development.copl.de | ./copli --nohttp >development.html.de \
	&& ./copli --nohttp index.copl.en index.html.en \
	&& ./copli --nohttp download.copl.en download.html.en \
	&& ./copli --nohttp development.copl.en development.html.en \
	&& ./copli --nohttp tutorials/modules/index.copl.en tutorials/modules/index.html.en \
	&& ./copli --nohttp tutorials/svn/index.copl.en tutorials/svn/index.html.en \
	&& ./copli --nohttp modules/audioplayer/index.copl.en modules/audioplayer/index.html.en \
	&& ./copli --nohttp modules/calculator/index.copl.en modules/calculator/index.html.en \
	&& ./copli --nohttp modules/calendar/index.copl.en modules/calendar/index.html.en \
	&& ./copli --nohttp modules/dcc/index.copl.en modules/dcc/index.html.en \
	&& ./copli --nohttp modules/filemanager/index.copl.en modules/filemanager/index.html.en \
	&& ./copli --nohttp modules/icq/index.copl.en modules/icq/index.html.en \
	&& ./copli --nohttp modules/irc/index.copl.en modules/irc/index.html.en \
	&& ./copli --nohttp modules/mailbox/index.copl.en modules/mailbox/index.html.en \
	&& ./copli --nohttp modules/pong/index.copl.en modules/pong/index.html.en \
	&& ./copli --nohttp modules/tictactoe/index.copl.en modules/tictactoe/index.html.en \
	&& ./copli --nohttp modules/timetable/index.copl.en modules/timetable/index.html.en
}

archive() 
{
	tar jcf homepage.tar.bz2 --exclude=*.svn* --exclude=copli --exclude=homepage.tar.bz2 --exclude=*upload.sh* --exclude=*.copl* .
}

upload()
{
	scp -r homepage.tar.bz2 schwering@shell.berlios.de:/home/groups/evi/htdocs \
	&& ssh -l schwering shell.berlios.de "cd /home/groups/evi/htdocs && (tar jxf homepage.tar.bz2; rm homepage.tar.bz2; cd sitemap_gen-1.4; python sitemap_gen.py --config=config.xml)" \
	&& rm homepage.tar.bz2
}

echo "Compiling ... " && compile
echo "Creating archive ..." && archive
echo "Copying to server ... " && upload
