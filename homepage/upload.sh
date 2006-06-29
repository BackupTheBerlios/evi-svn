compile_de() {
	iconv --from=utf-8 --to=iso-8859-1 index.copl.de | copli --nohttp >index.html.de 
}

compile() 
{
	echo -n "." && compile_de \
	&& echo -n "." && cd development && compile_de && cd .. \
	&& echo -n "." && cd download && compile_de && cd .. \
	&& echo -n "." && cd modules && compile_de && cd .. \
	&& echo -n "." && ./copli --nohttp index.copl.en index.html.en \
	&& echo -n "." && ./copli --nohttp modules/index.copl.en modules/index.html.en \
	&& echo -n "." && ./copli --nohttp download/index.copl.en download/index.html.en \
	&& echo -n "." && ./copli --nohttp development/index.copl.en development/index.html.en \
	&& echo -n "." && ./copli --nohttp tutorials/modules/index.copl.en tutorials/modules/index.html.en \
	&& echo -n "." && ./copli --nohttp tutorials/svn/index.copl.en tutorials/svn/index.html.en \
	&& echo -n "." && ./copli --nohttp modules/audioplayer/index.copl.en modules/audioplayer/index.html.en \
	&& echo -n "." && ./copli --nohttp modules/calculator/index.copl.en modules/calculator/index.html.en \
	&& echo -n "." && ./copli --nohttp modules/calendar/index.copl.en modules/calendar/index.html.en \
	&& echo -n "." && ./copli --nohttp modules/dcc/index.copl.en modules/dcc/index.html.en \
	&& echo -n "." && ./copli --nohttp modules/filemanager/index.copl.en modules/filemanager/index.html.en \
	&& echo -n "." && ./copli --nohttp modules/icq/index.copl.en modules/icq/index.html.en \
	&& echo -n "." && ./copli --nohttp modules/irc/index.copl.en modules/irc/index.html.en \
	&& echo -n "." && ./copli --nohttp modules/mailbox/index.copl.en modules/mailbox/index.html.en \
	&& echo -n "." && ./copli --nohttp modules/pong/index.copl.en modules/pong/index.html.en \
	&& echo -n "." && ./copli --nohttp modules/tictactoe/index.copl.en modules/tictactoe/index.html.en \
	&& echo -n "." && ./copli --nohttp modules/timetable/index.copl.en modules/timetable/index.html.en
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

echo -n "Compiling " && compile && echo "" \
&& echo "Creating archive ..." && archive\
&& echo "Copying to server ... " && upload
