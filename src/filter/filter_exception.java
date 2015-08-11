package filter;

@SuppressWarnings("serial")
class filter_exception extends Exception	//CUSTOM EXCEPTION CLASS FOR filter
{
	/*
	filter_exception()	//CONSTRUCTOR
	{
		super();
		//DO NOTHING;
	}
	//*/
	
	filter_exception(String name)	//CONSTRUCTOR
	{
		super(name);
	}
}