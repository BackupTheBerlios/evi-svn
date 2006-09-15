compile()
{
	for f in `find . -iname \*.copl.\?\?`
	do
		if [ -f $f ]
		then
			g=`echo $f | cut -d . -f -2`.html.en
			echo -n "Compiling $f to $g ... "
			iconv --from=utf-8 --to=iso-8859-1 $f | ./copli --nohttp >$g\
			&& echo "done" || echo "failed"
		fi
	done
}

clean()
{
	for f in `find . -iname \*.copl.\?\?`
	do
		if [ -f $f ]
		then
			g=`echo $f | cut -d . -f -2`.html.en
			echo -n "Removing $g ... "
			rm $g\
			&& echo "done" || echo "failed"
		fi
	done
}

archive() 
{
	tar jcf homepage.tar.bz2 --exclude=*.svn* --exclude=copli --exclude=homepage.tar.bz2 --exclude=*.sh --exclude=*.copl* .
}

upload()
{
	scp -r homepage.tar.bz2 schwering@shell.berlios.de:/home/groups/evi/htdocs \
	&& ssh -l schwering shell.berlios.de "cd /home/groups/evi/htdocs && (tar jxf homepage.tar.bz2; rm homepage.tar.bz2; cd sitemap_gen-1.4; python sitemap_gen.py --config=config.xml)" \
	&& rm homepage.tar.bz2
}

compile\
&& echo "Creating archive ..." && archive\
&& echo "Copying to server ... " && upload\
&& clean
