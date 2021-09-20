package com.empreendapp.collev.ui.adm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empreendapp.collev.R
import com.empreendapp.collev.util.LibraryClass
import android.widget.Toast
import com.empreendapp.collev.adapters.recycler.LocaisRecyclerAdapter
import com.empreendapp.collev.model.Local
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class LocaisFragment : Fragment() {
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var rvLocais: RecyclerView? = null
    private var adapter: LocaisRecyclerAdapter? = null
    private var locais: ArrayList<Local>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_locais, container, false)
        intViews(rootView)
        return rootView
    }

    private fun intViews(rootView: View) {
        rvLocais = rootView.findViewById(R.id.rv_locais)
        rvLocais!!.layoutManager = LinearLayoutManager(context)
        rvLocais!!.itemAnimator = DefaultItemAnimator()
        rvLocais!!.setHasFixedSize(true)

        database = LibraryClass.firebaseDB?.reference?.child("locais")
        locais = ArrayList()

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildAdded -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(Local::class.java)?.let {local ->
                    local.id = snapshot.key
                    locais!!.add(local)
                }
                updateList()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildChanged -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(Local::class.java)?.let { local ->
                    local.id = snapshot.key
                    locais?.forEachIndexed{ index, old -> if(old.id == local.id) locais!![index] = local}
                }
                updateList()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("DEBUG", "onChildRemoved: -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(Local::class.java)?.let { local ->
                    locais!!.removeIf { it.id == snapshot.key}
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildMoved: -> Key: ${snapshot.key} - value: ${snapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("W", "postLocals:onCancelled", error.toException())
                Toast.makeText(
                    context, "Failed to load locals.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        database?.addChildEventListener(childEventListener)
    }

    private fun updateList(){
        locais?.let { itens ->
            adapter = context?.let { ctx -> LocaisRecyclerAdapter(ctx, itens) }
            rvLocais!!.adapter = adapter
        }
    }
}