package com.empreendapp.collev.ui.adm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empreendapp.collev.R
import com.empreendapp.collev.adapters.recycler.LocaisRecyclerAdapter
import com.empreendapp.collev.util.LibraryClass

import android.widget.Toast

import com.google.firebase.database.DataSnapshot

import com.empreendapp.collev.util.FirebaseConnection
import com.google.firebase.database.DatabaseReference
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import java.lang.String


class LocaisFragment : Fragment() {
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var rvLocais: RecyclerView? = null
    private var adapter: LocaisRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_locais, container, false)
        intViews(rootView);
        return rootView
    }

    private fun intViews(rootView: View) {
        rvLocais = rootView.findViewById(R.id.rv_locais)
        rvLocais!!.layoutManager = LinearLayoutManager(context)
        rvLocais!!.itemAnimator = DefaultItemAnimator()
        rvLocais!!.setHasFixedSize(true)

//        val options: FirebaseRecyclerOptions<Local> = FirebaseRecyclerOptions.Builder<Local>()
//            .setQuery(
//                FirebaseDatabase.getInstance().getReference()
//                .child("locais"), Local::class.java)
//            .build()
//
//        adapter = LocaisRecyclerAdapter(options)
//        rvLocais!!.adapter = adapter
//
        val auth: FirebaseAuth? = FirebaseConnection.getFirebaseAuth()
        database = LibraryClass.firebaseDB?.reference

        if (auth != null) {
            if(auth.currentUser != null){
                if (auth != null) {
                    database!!.child("users").child(auth.uid.toString()).get()
                        .addOnCompleteListener(OnCompleteListener<DataSnapshot?> { task ->
                            if (!task.isSuccessful) {
                                Toast.makeText(context, "Firebase: Error getting data!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Firebase: " + String.valueOf(task.result?.getValue()), Toast.LENGTH_LONG).show()
                            }
                        })
                }
            }
        }


        //fetch();
        //adapter!!.notifyDataSetChanged()

        //Toast.makeText(context, "Itens: " + adapter!!.itemCount, Toast.LENGTH_LONG).show()
    }

    private fun lerLocais(){

    }

//    private fun fetch() {
//        val query: Query = FirebaseDatabase.getInstance()
//            .getReference()
//            .child("locais")
//
//        val options = FirebaseRecyclerOptions.Builder<Local>()
//            .setQuery(
//                query
//            ) { snapshot ->
//                Local(
//                    snapshot.child("nome").value.toString(),
//                    snapshot.child("descricao").value.toString()
//                )
//            }
//            .build()
//
//        adapter = object : FirebaseRecyclerAdapter<Local, LocalViewHolder>(options) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalViewHolder {
//                val view: View = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.item_local, parent, false)
//                return LocalViewHolder(view)
//            }
//
//            override fun onBindViewHolder(holder: LocalViewHolder, position: Int, model: Local) {
//                holder.setTxtNome(model.nome)
//                holder.root.setOnClickListener(View.OnClickListener {
//                    Toast.makeText(
//                        requireContext(),
//                        position.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                })
//            }
//        }
//        rvLocais!!.setAdapter(adapter)
//
//        Toast.makeText(context, "Adapeter is: " + adapter.toString(), Toast.LENGTH_LONG).show()
//        Toast.makeText(context, "Itens count: " + (adapter as FirebaseRecyclerAdapter<Local, LocalViewHolder>)?.itemCount, Toast.LENGTH_LONG).show()
//        Toast.makeText(context, "Snapshots Size: " + (adapter as FirebaseRecyclerAdapter<Local, LocalViewHolder>)?.snapshots.size, Toast.LENGTH_LONG).show()
//    }


//    private fun getLocaisInDB(n: Int): ArrayList<Local> {
//        var locais = ArrayList<Local>()
//        for(i in 1..n){
//            //locais.add(Local())
//        }
//        return locais
//    }

//    class LocalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var root: ConstraintLayout
//        var tvLocalNome: TextView
//
//        fun setTxtNome(string: String?) {
//            tvLocalNome.text = string
//        }
//
//        init {
//            root = itemView.findViewById(R.id.clLocalItem)
//            tvLocalNome = itemView.findViewById(R.id.tvLocalNome)
//        }
//    }
//

    override fun onStart() {
        super.onStart()
        //adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        //adapter!!.stopListening()
    }

}