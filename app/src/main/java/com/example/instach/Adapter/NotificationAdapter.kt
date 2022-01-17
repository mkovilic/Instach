package com.example.instach.Adapter

import android.app.Notification
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.instach.Model.Post
import com.example.instach.Model.User
import com.example.instach.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class NotificationAdapter(
    private val mContext: Context,
    private val mNotification: List<com.example.instach.Model.Notification>
) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {

        var postImage: ImageView
        var profileImage: CircleImageView
        var userName: TextView
        var text: TextView


        init {
            postImage = itemView.findViewById(R.id.notification_post_image)
            profileImage = itemView.findViewById(R.id.notification_profile_image)
            userName = itemView.findViewById(R.id.username_notification)
            text = itemView.findViewById(R.id.comment_notification)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.notifications_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notifcation = mNotification[position]

        if(notifcation.getText().equals("started following you"))
        {
            holder.text.text = "started following you"
        }else if(notifcation.getText().equals("liked your post")){
            holder.text.text = "liked your post"

        }else if(notifcation.getText().contains("commented:")){
            holder.text.text = notifcation.getText()
                .replace("commented:","commented : ")

        }else{
            holder.text.text = notifcation.getText()
        }

        userInfo(holder.profileImage, holder.userName, notifcation.getUserId())

        if (notifcation.getIsPost()) {
            holder.postImage.visibility = View.VISIBLE
            getPostImage(holder.postImage, notifcation.getPostId())
        }else{
            holder.postImage.visibility = View.GONE

        }

        holder.itemView.setOnClickListener{view ->
            if (notifcation.getIsPost()){
                val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("postId", notifcation.getPostId())
                editor.apply()
                view.findNavController().navigate(R.id.action_navigation_profile_to_postDetailsFragment)
            }else{
                val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("profileId", notifcation.getUserId())
                editor.apply()
                view.findNavController().navigate(R.id.action_navigation_notifications_to_navigation_profile)
            }
        }
    }

    override fun getItemCount(): Int {
        return mNotification.size
    }

    private fun userInfo(imageView: ImageView, userName: TextView, publisherId: String) {
        val userRef =
            FirebaseDatabase.getInstance().reference.child("Users").child(publisherId)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                        .into(imageView)
                    userName.text = user.getUsername()

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun getPostImage(imageView: ImageView, postId: String) {
        val postRef =
            FirebaseDatabase.getInstance().reference.child("Posts").child(postId)

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                if (snapshot.exists()) {
                    val post = snapshot.getValue<Post>(Post::class.java)
                    Picasso.get().load(post!!.getPostImage()).placeholder(R.drawable.profile)
                        .into(imageView)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}