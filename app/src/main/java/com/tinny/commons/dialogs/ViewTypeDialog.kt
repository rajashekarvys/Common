package com.tinny.commons.dialogs


/*class ViewTypeDialog : DialogFragment() {

    var listener: ViewTypeInterface? = null

    interface ViewTypeInterface {
        fun update(pos: Int)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as ViewTypeInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_type_dailog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeViewTypeGroup.check(changeViewTypeGroup.getChildAt(arguments!!.getInt("POS")).id)
        changeViewTypeGroup.setOnCheckedChangeListener { group, checkedId ->
            val index = changeViewTypeGroup.indexOfChild(changeViewTypeGroup.findViewById(checkedId))
            listener!!.update(index)
            dismiss()

        }
    }

}*/
